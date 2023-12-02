package id.nexteam.batch_demo.config;

import id.nexteam.batch_demo.entities.Song;
import id.nexteam.batch_demo.processors.SongProcessor;
import id.nexteam.batch_demo.repositories.SongRepository;
import id.nexteam.batch_demo.tasks.ReaderTask;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;

@Configuration
@Import(DataSourceConfiguration.class)
@EnableBatchProcessing(isolationLevelForCreate = "ISOLATION_DEFAULT", transactionManagerRef = "jpaTransactionManager")
@EnableJpaRepositories(basePackages = "id.nexteam.batch_demo.repositories")
public class SpringBatchConfig {

    @Bean
    public FlatFileItemReader<Song> reader() {
        FlatFileItemReader<Song> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/universal_top_spotify_songs.csv"));
        itemReader.setName("csv-reader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<Song> lineMapper() {

        DefaultLineMapper<Song> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("name", "spotify_id");

        BeanWrapperFieldSetMapper<Song> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Song.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public RepositoryItemWriter<Song> writer(SongRepository repository) {
        return new RepositoryItemWriterBuilder<Song>().repository(repository).methodName("save").build();
    }

    @Bean
    public JpaTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public Job job(JobRepository jobRepository, JpaTransactionManager jpaTransactionManager, FlatFileItemReader<Song> reader, RepositoryItemWriter<Song> writer) {
        return new JobBuilder("ioSampleJob", jobRepository)
                .start(new StepBuilder("step1", jobRepository)
                        .<Song, Song>chunk(2, jpaTransactionManager)
                        .reader(reader)
                        .processor(new SongProcessor())
                        .writer(writer)
                        .taskExecutor(new SimpleAsyncTaskExecutor())
                        .build())
                .build();
    }

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

}
