package biz.deinum.batch.item.excel.streaming;

import biz.deinum.batch.item.excel.AbstractExcelItemReader;
import biz.deinum.batch.item.excel.AbstractExcelItemReaderTests;
import org.springframework.core.io.ClassPathResource;

class StreamingXlsxItemReaderTest extends AbstractExcelItemReaderTests {

	@Override
	protected void configureItemReader(AbstractExcelItemReader<String[]> itemReader) {
		itemReader.setResource(new ClassPathResource("player.xlsx"));
	}

	@Override
	protected AbstractExcelItemReader<String[]> createExcelItemReader() {
		return new StreamingXlsxItemReader<>();
	}
}