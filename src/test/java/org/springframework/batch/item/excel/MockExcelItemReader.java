package org.springframework.batch.item.excel;

import org.springframework.core.io.ByteArrayResource;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * @author Marten Deinum
 */
public class MockExcelItemReader<T> extends AbstractExcelItemReader<T> {


    private final List<MockSheet> sheets;

    public MockExcelItemReader(MockSheet sheet) {
        this(Collections.singletonList(sheet));
    }

    public MockExcelItemReader(List<MockSheet> sheets) {
        this.sheets=sheets;
        super.setResource(new ByteArrayResource(new byte[0]));
    }

    @Override
    protected Sheet getSheet(int sheet) {
        return sheets.get(sheet);
    }

    @Override
    protected int getNumberOfSheets() {
        return sheets.size();
    }

    @Override
    protected void openExcelFile(InputStream resource) throws Exception {

    }

    @Override
    protected void doClose() throws Exception {
        sheets.clear();
    }
}
