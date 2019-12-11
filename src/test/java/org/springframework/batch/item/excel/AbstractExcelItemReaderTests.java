/*
 * Copyright 2006-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.batch.item.excel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ReflectionTestUtils;
import org.springframework.batch.item.excel.mapping.PassThroughRowMapper;
import org.springframework.core.io.ClassPathResource;

/**
 * Base class for testing Excel based item readers.
 *
 * @author Marten Deinum
 */
public abstract class AbstractExcelItemReaderTests  {

    protected final Log logger = LogFactory.getLog(this.getClass());

    protected AbstractExcelItemReader<String[]> itemReader;

    @BeforeEach
    public void setup() throws Exception {
        this.itemReader = createExcelItemReader();
        this.itemReader.setLinesToSkip(1); //First line is column names
        this.itemReader.setResource(new ClassPathResource("player.xls"));
        this.itemReader.setRowMapper(new PassThroughRowMapper());
        this.itemReader.setSkippedRowsCallback(rs -> logger.info("Skipping: " + Arrays.toString(rs.getCurrentRow())));
        configureItemReader(this.itemReader);
        this.itemReader.afterPropertiesSet();
        ExecutionContext executionContext = new ExecutionContext();
        this.itemReader.open(executionContext);
    }

    protected void configureItemReader(AbstractExcelItemReader itemReader) {
    }

    @AfterEach
    public void after() throws Exception {
        this.itemReader.close();
    }

    @Test
    public void readExcelFile() throws Exception {
        assertEquals(3, this.itemReader.getNumberOfSheets());
        String[] row;
        do {
            row = this.itemReader.read();
            this.logger.debug("Read: " + Arrays.toString(row));
            if (row != null) {
                assertEquals(6, row.length);
            }
        } while (row != null);
        int readCount = (Integer) ReflectionTestUtils.getField(this.itemReader, "currentItemCount" );
        assertEquals(4321, readCount);
    }

    @Test
    public void testRequiredProperties() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
            final AbstractExcelItemReader<String[]> reader = createExcelItemReader();
            reader.afterPropertiesSet();
        });
    }

    protected abstract AbstractExcelItemReader<String[]> createExcelItemReader();

}
