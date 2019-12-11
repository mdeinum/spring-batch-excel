# spring-batch-excel

[![CircleCI](https://circleci.com/gh/mdeinum/spring-batch-excel.svg?style=svg)](https://circleci.com/gh/mdeinum/spring-batch-excel)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/c52a51f788a543a9964cb8a148cfcd92)](https://www.codacy.com/manual/mdeinum/spring-batch-excel?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=mdeinum/spring-batch-excel&amp;utm_campaign=Badge_Grade)

Spring Batch extension which contains an `ItemReader` implementation for Excel based on [Apache POI][1]. 

## Configuration

Next to the [configuration of Spring Batch](http://docs.spring.io/spring-batch/reference/html/configureJob.html) one needs to configure the `PoiItemReader`.

Configuration of can be done in XML or using Java Config.

#### XML

    <bean id="excelReader" class="org.springframework.batch.item.excel.poi.PoiItemReader" scope="step">
        <property name="resource" value="classpath:/path/to/your/excel/file" />
        <property name="rowMapper">
            <bean class="org.springframework.batch.item.excel.mapping.PassThroughRowMapper" />
        </property>
    </bean>

#### Java Config

    @Bean
    @StepScope
    public PoiItemReader excelReader() {
        PoiItemReader reader = new PoiItemReader();
        reader.setResource(new ClassPathResource("/path/to/your/excel/file"));
        reader.setRowMapper(rowMapper());
        return reader;
    }

    @Bean
    public RowMapper rowMapper() {
        return new PassThroughRowMapper();
    }

The reader takes a `resource` and a `rowMapper`. The `resource` is the location of the excel file to read and the `rowMapper` transforms the rows in excel to an object which you can use in the rest of the process.

Optionally one can also set the `skippedRowsCallback`, `linesToSkip`, `strict` and `rowSetFactory` properties.

##### skippedRowsCallback
When rows are skipped an optional `org.springframework.batch.item.excel.RowCallbackHandler` is called with the skipped row. This comes in handy when one needs to write the skipped rows to another file or create some logging.

##### linesToSkip
The number of lines to skip, this applies to each sheet in the Excel file, can be useful if the first couple of lines provide header information.

##### strict
By default `true`. This controls wether or not an exception is thrown if the file doesn't exists, by default an exception will be thrown.

##### rowSetFactory
For reading rows a `RowSet` abstraction is used. To construct a `RowSet` for the current `Sheet` a `RowSetFactory` is needed. The `DefaultRowSetFactory` constructs a `DefaultRowSet` and `DefaultRowSetMetaData`. For construction of the latter a `ColumnNameExtractor` is needed. At the moment there are 2 implementations

 - `StaticColumnNameExtractor` uses a preset list of column names.
 - `RowNumberColumnNameExtractor` (**the default**) reads a given row (default 0) to determine the column names of the current sheet

## RowMappers
Next to the default `ItemReader` implementations there are also 2 `RowMapper` implementations.

### PassThroughRowMapper
Transforms the read row from excel into a `String[]`.

### BeanWrapperRowMapper
Uses a `BeanWrapper` to convert a given row into an object. Uses the column names of the given `RowSet` to map column to properties of the `targetType` or prototype bean.

    <bean id="excelReader" class="org.springframework.batch.item.excel.poi.PoiItemReader" scope="step">
        <property name="resource" value="classpath:/path/to/your/excel/file" />
        <property name="rowMapper">
            <bean class="org.springframework.batch.item.excel.mapping.BeanWrapperRowMapper">
                <property name="targetType" value="com.your.package.Player" />
            <bean>
        </property>
    </bean>

[1]: http://poi.apache.org
