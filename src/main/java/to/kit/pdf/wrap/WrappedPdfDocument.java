package to.kit.pdf.wrap;

import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;

/**
 * Wrapped PdfDocument.
 * @author Hidetaka Sasai
 */
public class WrappedPdfDocument extends PdfDocument {
	@Override
	public boolean addProducer() {
		super.info.put(PdfName.PRODUCER, new PdfString("Nach."));
		return true;
	}
}
