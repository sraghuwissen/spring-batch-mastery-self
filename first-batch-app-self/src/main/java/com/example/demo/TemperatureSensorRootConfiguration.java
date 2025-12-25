package com.example.demo;

import javax.sql.DataSource;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.BatchDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.sql.init.DatabaseInitializationMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@PropertySource("classpath:application.properties")
public class TemperatureSensorRootConfiguration extends DefaultBatchConfiguration{
	
	

	
    @Value("classpath:input/HTE2NP.txt")
    private Resource rawDailyInputResource;

    @Value("file:HTE2NP.xml")
    private WritableResource aggregatedDailyOutputXmlResource;

    @Value("file:HTE2NP-anomalies.csv")
    private WritableResource anomalyDataResource;
	

	 /**
     * Due to usage of {@link DefaultBatchConfiguration}, dataSource need to be manually created.
     * When {@link org.springframework.batch.core.configuration.annotation.EnableBatchProcessing} is used,
     * it would have been auto-created
     */
    @Bean
    public DataSource dataSource(@Value("${spring.datasource.driverClassName}") String driverClassName,
                                 @Value("${spring.datasource.url}") String url,
                                 @Value("${spring.datasource.username}") String username,
                                 @Value("${spring.datasource.password}") String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
	
    // Simplest possible transaction manager configuration to let Spring Batch persist metadata about job / step completions
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        JdbcTransactionManager transactionManager = new JdbcTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    /**
     * Due to usage of {@link DefaultBatchConfiguration}, db initializer need to defined in order for Spring Batch
     * to consider initializing the schema on the first usage. In case of
     * {@link org.springframework.batch.core.configuration.annotation.EnableBatchProcessing} usage, it would have
     * been resolved with 'spring.batch.initialize-schema' property
     */
    @Bean
    public BatchDataSourceScriptDatabaseInitializer batchDataSourceInitializer(DataSource dataSource,
                                                                               BatchProperties properties) {
        return new BatchDataSourceScriptDatabaseInitializer(dataSource, properties.getJdbc());
    }

    /**
     * Due to usage of {@link DefaultBatchConfiguration}, we need to explicitly (programmatically) set initializeSchema
     * mode, and we are taking this parameter from the configuration wile, defined at {@link PropertySource} on class level;
     * In case we'd use {@link org.springframework.batch.core.configuration.annotation.EnableBatchProcessing}, having
     * 'spring.batch.initialize-schema' property would be enough
     */
    @Bean
    public BatchProperties batchProperties(@Value("${batch.db.initialize-schema}") DatabaseInitializationMode initializationMode) {
        BatchProperties properties = new BatchProperties();
        properties.getJdbc().setInitializeSchema(initializationMode);
        return properties;
    }

    
	@Bean
	public Job temperatureSensorJob(JobRepository jobRepository,
			Step aggregateSensorStep) {
		
		return new JobBuilder("temperatureSensorJob", jobRepository).start(aggregateSensorStep).build();
		
	}
	
	@Bean
	@Qualifier("aggregateSensorStep") 
	public Step aggregateSensorStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		
		return new StepBuilder("aggregate-sensor", jobRepository)
				.<DailySensorData, DailyAggregatedSensorData>chunk(1, transactionManager)
				.reader(new FlatFileItemReaderBuilder<DailySensorData>().name("dailySensorDataReader").resource(rawDailyInputResource)
						.lineMapper(new SensorDataTextMapper()).build())
				.processor(new RawToAggregateSensorDataProcessor())
				.writer( new StaxEventItemWriterBuilder().name("dailyAggregatedSensorDataWriter").marshaller(DailyAggregatedSensorData.getMarshaller())
						.resource(aggregatedDailyOutputXmlResource).overwriteOutput(true).rootTagName("data").build())
				.build();
		
		
	}
	
	

}