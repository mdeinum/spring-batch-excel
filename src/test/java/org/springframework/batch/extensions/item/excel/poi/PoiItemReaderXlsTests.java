package org.springframework.batch.extensions.item.excel.poi;

import org.springframework.batch.extensions.item.excel.AbstractExcelItemReader;
import org.springframework.batch.extensions.item.excel.AbstractExcelItemReaderTests;

public class PoiItemReaderXlsTests extends AbstractExcelItemReaderTests {

    @Override
    protected AbstractExcelItemReader<String[]> createExcelItemReader() {
        return new PoiItemReader<>();
    }
}
