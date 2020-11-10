package biz.deinum.batch.item.excel.poi;

import biz.deinum.batch.item.excel.AbstractExcelItemReader;
import biz.deinum.batch.item.excel.AbstractExcelItemReaderTests;

public class PoiItemReaderXlsTests extends AbstractExcelItemReaderTests {

    @Override
    protected AbstractExcelItemReader<String[]> createExcelItemReader() {
        return new PoiItemReader<>();
    }
}
