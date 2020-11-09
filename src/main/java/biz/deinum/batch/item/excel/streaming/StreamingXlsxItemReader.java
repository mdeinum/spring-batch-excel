package biz.deinum.batch.item.excel.streaming;

import biz.deinum.batch.item.excel.AbstractExcelItemReader;
import biz.deinum.batch.item.excel.Sheet;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.springframework.core.io.Resource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple streaming reader without Apache POI.
 *
 * @param <T> the type
 * @author Marten Deinum
 * @since 2.0.0
 **/
public class StreamingXlsxItemReader<T> extends AbstractExcelItemReader<T> {

	private final List<StreamingSheet> sheets = new ArrayList<>();

	private OPCPackage pkg;
	private InputStream inputStream;

	@Override
	protected Sheet getSheet(int sheet) {
		return this.sheets.get(sheet);
	}

	@Override
	protected int getNumberOfSheets() {
		return this.sheets.size();
	}

	@Override
	protected void openExcelFile(Resource resource, String password) throws Exception {
		try {
			File file = resource.getFile();
			this.pkg = OPCPackage.open(file, PackageAccess.READ);
		} catch (FileNotFoundException e) {
			this.inputStream = resource.getInputStream();
			this.pkg = OPCPackage.open(this.inputStream);
		}
		XSSFReader reader = new XSSFReader(pkg);
		initSheets(reader, pkg);
	}

	private void initSheets(XSSFReader reader, OPCPackage pkg) throws IOException, InvalidFormatException {
		XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) reader.getSheetsData();
		SharedStrings sharedStrings;
		try {
			sharedStrings = new ReadOnlySharedStringsTable(pkg);
		} catch (SAXException e) {
			throw new IllegalStateException("Cannot read shared-strings-table.", e);
		}
		Styles styles = reader.getStylesTable();
		while (iter.hasNext()) {
			InputStream is = iter.next();
			String name = iter.getSheetName();
			sheets.add(new StreamingSheet(name, is, sharedStrings, styles));
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Prepared " + this.sheets.size() + " sheets.");
		}
	}

	@Override
	protected void doClose() throws Exception {
		this.pkg.revert();

		for(StreamingSheet sheet : sheets ) {
			sheet.close();
		}
		this.sheets.clear();

		if (this.inputStream != null) {
			this.inputStream.close();
			this.inputStream = null;
		}
		super.doClose();
	}
}
