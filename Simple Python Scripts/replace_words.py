import fitz  # PyMuPDF
from reportlab.pdfgen import canvas
from reportlab.lib.pagesizes import letter
import io

def replace_words(text, words_to_replace, replacement_words):
    for word, replacement in zip(words_to_replace, replacement_words):
        text = text.replace(word, replacement)
    return text


def extract_text_from_pdf(pdf_path):
    doc = fitz.open(pdf_path)
    pages_text = [page.get_text() for page in doc]
    doc.close()
    return pages_text


def create_pdf_from_text(pages_text, output_pdf_path):
    packet = io.BytesIO()
    c = canvas.Canvas(packet, pagesize=letter)
    width, height = letter

    for page_text in pages_text:
        y = height - 40  # Starting y-position
        for line in page_text.split('\n'):
            c.drawString(40, y, line)
            y -= 14  # Line spacing
            if y < 40:  # Create a new page if needed
                c.showPage()
                y = height - 40
        c.showPage()
    c.save()

    # Save to final file
    with open(output_pdf_path, 'wb') as f:
        f.write(packet.getvalue())


def replace_words_in_pdf(input_pdf, output_pdf, words_to_replace, replacement_words):
    pages_text = extract_text_from_pdf(input_pdf)
    updated_pages = [
        replace_words(text, words_to_replace, replacement_words)
        for text in pages_text
    ]
    create_pdf_from_text(updated_pages, output_pdf)


# ==== Example Usage ====

input_pdf = "" # I need something to replace words in
output_pdf = "processesed_data/output.pdf"
words_to_replace = []                                             
replacement_words = []

replace_words_in_pdf(input_pdf, output_pdf, words_to_replace, replacement_words)
