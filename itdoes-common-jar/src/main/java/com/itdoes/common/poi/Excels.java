package com.itdoes.common.poi;

import java.io.File;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itdoes.common.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class Excels {
	private static final Logger LOGGER = LoggerFactory.getLogger(Excels.class);

	public static Excels create() {
		return new Excels();
	}

	private String password = null;
	private boolean readOnly = true;

	private boolean includeSheetNames;
	private boolean includeSheetNamesSetted;

	private boolean formulasNotResults;
	private boolean formulasNotResultsSetted;

	private boolean includeHeadersFooters;
	private boolean includeHeadersFootersSetted;

	private boolean includeCellComments;
	private boolean includeCellCommentsSetted;

	private boolean includeBlankCells;
	private boolean includeBlankCellsSetted;

	private boolean includeTextBoxes;
	private boolean includeTextBoxesSetted;

	public String getText(String xlsFilename) {
		return getText(new File(xlsFilename));
	}

	public String getText(File xlsFile) {
		final Workbook workbook;
		try {
			workbook = WorkbookFactory.create(xlsFile, password, readOnly);
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			throw Exceptions.unchecked(e, IllegalArgumentException.class);
		}

		if (workbook instanceof HSSFWorkbook) { // xls
			final HSSFWorkbook hssfWorkbook = (HSSFWorkbook) workbook;
			final ExcelExtractor extractor = new ExcelExtractor(hssfWorkbook);

			configExtractor(extractor);
			if (includeBlankCellsSetted) {
				extractor.setIncludeBlankCells(includeBlankCells);
			}

			final String text = extractor.getText();

			try {
				extractor.close();
			} catch (IOException e) {
				LOGGER.warn("Error in closing Extractor for: " + xlsFile, e);
			}

			return text;
		} else if (workbook instanceof XSSFWorkbook) { // xlsx
			final XSSFWorkbook xssfWorkbook = (XSSFWorkbook) workbook;
			final XSSFExcelExtractor extractor = new XSSFExcelExtractor(xssfWorkbook);

			configExtractor(extractor);
			if (includeTextBoxesSetted) {
				extractor.setIncludeTextBoxes(includeTextBoxes);
			}

			final String text = extractor.getText();

			try {
				extractor.close();
			} catch (IOException e) {
				LOGGER.warn("Error in closing Extractor for: " + xlsFile, e);
			}

			return text;
		} else {
			throw new IllegalArgumentException("Workbook " + workbook + " is neither HSSFWorkbook, nor XSSFWorkbook");
		}
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Excels setIncludeSheetNames(boolean includeSheetNames) {
		this.includeSheetNames = includeSheetNames;
		this.includeSheetNamesSetted = true;
		return this;
	}

	public Excels setFormulasNotResults(boolean formulasNotResults) {
		this.formulasNotResults = formulasNotResults;
		this.formulasNotResultsSetted = true;
		return this;
	}

	public Excels setIncludeHeadersFooters(boolean includeHeadersFooters) {
		this.includeHeadersFooters = includeHeadersFooters;
		this.includeHeadersFootersSetted = true;
		return this;
	}

	public Excels setIncludeCellComments(boolean includeCellComments) {
		this.includeCellComments = includeCellComments;
		this.includeCellCommentsSetted = true;
		return this;
	}

	public Excels setIncludeBlankCells(boolean includeBlankCells) {
		this.includeBlankCells = includeBlankCells;
		this.includeBlankCellsSetted = true;
		return this;
	}

	public Excels setIncludeTextBoxes(boolean includeTextBoxes) {
		this.includeTextBoxes = includeTextBoxes;
		this.includeTextBoxesSetted = true;
		return this;
	}

	private void configExtractor(org.apache.poi.ss.extractor.ExcelExtractor extractor) {
		if (includeSheetNamesSetted) {
			extractor.setIncludeSheetNames(includeSheetNames);
		}
		if (formulasNotResultsSetted) {
			extractor.setFormulasNotResults(formulasNotResults);
		}
		if (includeHeadersFootersSetted) {
			extractor.setIncludeHeadersFooters(includeHeadersFooters);
		}
		if (includeCellCommentsSetted) {
			extractor.setIncludeCellComments(includeCellComments);
		}
	}

	private Excels() {
	}
}
