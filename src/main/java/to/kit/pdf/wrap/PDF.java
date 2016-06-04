package to.kit.pdf.wrap;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * PdfWriterのラッパー.
 * @author Hidetaka Sasai
 */
public class PDF extends PdfWriter {
	private Document doc = new Document();
	private Map<String, BaseFont> fontMap = new HashMap<>();
	private float lastLeading;

	private BaseFont getFont(String fontName) {
		BaseFont font;
		if (this.fontMap.containsKey(fontName)) {
			font = this.fontMap.get(fontName);
		} else {
			try {
				font = BaseFont.createFont(fontName, "UniJIS-UTF8-H", BaseFont.NOT_EMBEDDED);
			} catch (DocumentException | IOException e) {
				e.printStackTrace();
				font = null;
			}
		}
		return font;
	}

	public void addNewPage() {
		this.doc.newPage();
	}

	public void beginText() {
		PdfContentByte content = getDirectContent();

		content.beginText();
	}

	public void endText() {
		PdfContentByte content = getDirectContent();

		content.endText();
	}

	public void setFont(String fontName, float size) {
		BaseFont font = getFont(fontName);

		if (font != null) {
			PdfContentByte content = getDirectContent();

			content.setFontAndSize(font, size);
			this.lastLeading = size;
		}
	}

	public void drawString(String str) {
		PdfContentByte content = getDirectContent();

		content.showText(str);
	}

	public void drawString(String str, float x, float y) {
		float h = getPageSize().getHeight();
		PdfContentByte content = getDirectContent();

		content.setTextMatrix(x, h - y);
		drawString(str);
	}

	public void newLine(float leading) {
		PdfContentByte content = getDirectContent();

		content.setLeading(leading);
		content.newlineText();
	}

	public void newLine() {
		newLine(this.lastLeading);
	}

	public void closeDocument() {
		this.doc.close();
	}

	/**
	 * インスタンス生成.
	 * @param title
	 * @param author
	 * @param out
	 */
	public PDF(final String title, final String author, final OutputStream out) {
		super(new WrappedPdfDocument(), out);
		this.doc.addDocListener(super.pdf);
		try {
			super.pdf.addWriter(this);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		this.addViewerPreference(PdfName.PRINTSCALING, PdfName.NONE);
		this.doc.addTitle(title);
		this.doc.addAuthor(author);
		this.doc.open();
	}
}
