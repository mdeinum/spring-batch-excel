package org.springframework.batch.extensions.excel.poi;

import org.springframework.batch.extensions.excel.AbstractExcelItemReader;
import org.springframework.batch.extensions.excel.AbstractExcelItemReaderTests;

public class PoiItemReaderXlsTests extends AbstractExcelItemReaderTests {

    @Override
    protected AbstractExcelItemReader<String[]> createExcelItemReader() {
        return new PoiItemReader<>();
    }
}
