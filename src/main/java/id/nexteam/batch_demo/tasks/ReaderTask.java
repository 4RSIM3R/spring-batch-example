package id.nexteam.batch_demo.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class ReaderTask implements Tasklet, StepExecutionListener {

    private final Logger logger = LoggerFactory.getLogger(ReaderTask.class);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.debug("Execute The ReaderTask");
        return RepeatStatus.FINISHED;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        logger.debug("Before The ReaderTask");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.debug("Exit The ReaderTask");
        return ExitStatus.COMPLETED;
    }
}
