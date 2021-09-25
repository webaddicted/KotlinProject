package com.webaddicted.kotlinproject.view.fragment

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.ViewDataBinding
import com.itextpdf.text.*
import com.itextpdf.text.html.WebColors
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmDynamicLayoutBinding
import com.webaddicted.kotlinproject.databinding.RowDynamicLayoutBinding
import com.webaddicted.kotlinproject.global.common.FileHelper
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.PermissionHelper
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment
import java.io.*
import java.util.*


class DynamicLayoutFrm : BaseFragment() {
    private lateinit var pdfFile: File
    private lateinit var mBinding: FrmDynamicLayoutBinding

    companion object {
        val TAG = DynamicLayoutFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): DynamicLayoutFrm {
            val fragment = DynamicLayoutFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_dynamic_layout
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmDynamicLayoutBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.dynamic_title)
        pdfFile = File(FileHelper.subFolder(), "Created.pdf")
        addView()
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnCreatePdf.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.btn_create_pdf -> checkPermission()
        }
    }

    private fun addView() {
        for (i in 0..7) {
            val rowBinding = GlobalUtility.getLayoutBinding(
                activity,
                R.layout.row_dynamic_layout
            ) as RowDynamicLayoutBinding
            rowBinding.txtDate.text = "19/07/2017" + " " + "to"
            rowBinding.txtAccountNo.text = "ICICI Bank 12355252255455"
            rowBinding.txtVehType.text = "sales"
            rowBinding.txtVehNo.text = "25"
            rowBinding.txtcredit.text = "25000"
            rowBinding.txtDebit.text = "40000"

            mBinding.linearAddLayout.addView(rowBinding.root)
        }
        mBinding.totalCredit.text = (25000 * 8).toString() + ""
        mBinding.totalDedit.text = (40000 * 8).toString() + ""
    }


    private fun checkPermission() {
        val locationList = ArrayList<String>()
        locationList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        locationList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        PermissionHelper.requestMultiplePermission(
            mActivity,
            locationList,
            object : PermissionHelper.Companion.PermissionListener {
                override fun onPermissionGranted(mCustomPermission: List<String>) {
                    FileHelper.createApplicationFolder()
                    if (pdfFile.exists()) openPDF(pdfFile)
                    else createPDF()
                }

                override fun onPermissionDenied(mCustomPermission: List<String>) {

                }
            })
    }

    @Throws(FileNotFoundException::class, DocumentException::class)
    fun createPDF() {
        val myColor = WebColors.getRGBColor("#9E9E9E")
        val myColor1 = WebColors.getRGBColor("#757575")
        //create document file
        val doc = Document()
        try {

            val fOut = FileOutputStream(pdfFile)
            PdfWriter.getInstance(doc, fOut)
            //open the document
            doc.open()
            //create table
            val pt = PdfPTable(3)
            pt.widthPercentage = 100F
            val fl = floatArrayOf(20f, 45f, 35f)
            pt.setWidths(fl)
            var cell = PdfPCell()
            cell.border = Rectangle.NO_BORDER

            //set drawable in cell
            val myImage =
                ContextCompat.getDrawable(mActivity,R.drawable.logo)
            val bitmap =
                (myImage as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val bitmapdata = stream.toByteArray()
            try {
                val bgImage = Image.getInstance(bitmapdata)
                bgImage.setAbsolutePosition(330f, 642f)
                cell.addElement(bgImage)
                pt.addCell(cell)
                cell = PdfPCell()
                cell.border = Rectangle.NO_BORDER
                //    Heading name ur Firm Name
                cell.addElement(Paragraph(resources.getString(R.string.app_name)))
                cell.addElement(Paragraph(""))
                cell.addElement(Paragraph(""))
                pt.addCell(cell)
                cell = PdfPCell(Paragraph(""))
                cell.border = Rectangle.NO_BORDER
                pt.addCell(cell)
                val pTable = PdfPTable(1)
                pTable.widthPercentage = 100F
                cell = PdfPCell()
                cell.colspan = 1
                cell.addElement(pt)
                pTable.addCell(cell)
                // no Of Column no a field
                val table = PdfPTable(7)
                //width of  every coloumn
                val columnWidth = floatArrayOf(12f, 30f, 30f, 20f, 20f, 30f, 30f)
                table.setWidths(columnWidth)
                cell = PdfPCell()
                cell.backgroundColor = myColor
                cell.colspan = 7
                cell.addElement(pTable)
                table.addCell(cell)
                cell = PdfPCell(Phrase(" "))
                cell.colspan = 7
                table.addCell(cell)
                cell = PdfPCell()
                cell.colspan = 7
                cell.backgroundColor = myColor1
                // colom  Heading name Defile Here
                cell = PdfPCell(Phrase("Sr No."))
                cell.backgroundColor = myColor1
                table.addCell(cell)
                cell = PdfPCell(Phrase("Date"))
                cell.backgroundColor = myColor1
                table.addCell(cell)
                cell = PdfPCell(Phrase("Particulars"))
                cell.backgroundColor = myColor1
                table.addCell(cell)
                cell = PdfPCell(Phrase("Voucher Type"))
                cell.backgroundColor = myColor1
                table.addCell(cell)
                cell = PdfPCell(Phrase("Voucher No."))
                cell.backgroundColor = myColor1
                table.addCell(cell)
                cell = PdfPCell(Phrase("Debit"))
                cell.backgroundColor = myColor1
                table.addCell(cell)
                cell = PdfPCell(Phrase("Credit"))
                cell.backgroundColor = myColor1
                table.addCell(cell)


                //table.setHeaderRows(3);
                cell = PdfPCell()
                cell.colspan = 7
                //                custDate.setText("19/07/2017" + " " + "to");
//                custPart.setText("ICICI Bank 12355252255455");
//                custVchTyp.setText("Sales");
//                custVchNo.setText("25");
//                custCredit.setText("25000");
//                custDebit.setText("40000");
                for (i in 1..8) {
                    table.addCell(i.toString())
                    table.addCell("19/07/2017" + " " + "to")
                    table.addCell("ICICI Bank 12355252255455")
                    table.addCell("Sales")
                    table.addCell("25")
                    table.addCell("25000")
                    table.addCell("40000")
                }
                val ftable = PdfPTable(7)
                ftable.widthPercentage = 100F
                //                float[] columnWidthaa = new float[]{30, 10, 30, 10, 30, 10};
                val columnWidthaa = floatArrayOf(92f, 0f, 0f, 0f, 20f, 30f, 30f)
                ftable.setWidths(columnWidthaa)
                cell = PdfPCell()
                cell.colspan = 7
                cell.backgroundColor = myColor1
                cell = PdfPCell(Phrase("Total Amount"))
                cell.border = Rectangle.NO_BORDER
                cell.backgroundColor = myColor1
                ftable.addCell(cell)
                cell = PdfPCell(Phrase(""))
                cell.border = Rectangle.NO_BORDER
                cell.backgroundColor = myColor1
                ftable.addCell(cell)
                cell = PdfPCell(Phrase(""))
                cell.border = Rectangle.NO_BORDER
                cell.backgroundColor = myColor1
                ftable.addCell(cell)
                cell = PdfPCell(Phrase(""))
                cell.border = Rectangle.NO_BORDER
                cell.backgroundColor = myColor1
                ftable.addCell(cell)
                cell = PdfPCell(Phrase(""))
                cell.border = Rectangle.NO_BORDER
                cell.backgroundColor = myColor1
                ftable.addCell(cell)
                cell = PdfPCell(Phrase("320000"))
                cell.backgroundColor = myColor1
                ftable.addCell(cell)
                cell = PdfPCell(Phrase("200000"))
                cell.backgroundColor = myColor1
                ftable.addCell(cell)
                //                cell = new PdfPCell(new Phrase(totalDedit+""));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setBackgroundColor(myColor1);
//                ftable.addCell(cell);
                cell = PdfPCell(Paragraph(""))
                cell.colspan = 7
                ftable.addCell(cell)
                cell = PdfPCell()
                cell.colspan = 7
                cell.addElement(ftable)
                table.addCell(cell)
                doc.add(table)
                openPDF(pdfFile)
            } catch (de: DocumentException) {
                Log.e("PDFCreator", "DocumentException:$de")
            } catch (e: IOException) {
                Log.e("PDFCreator", "ioException:$e")
            } finally {
                doc.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openPDF(file: File) {
        var intent: Intent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                val uri =
                    FileProvider.getUriForFile(
                        mActivity,
                        activity?.packageName + ".provider",
                        file
                    )
                intent = Intent(Intent.ACTION_VIEW)
                intent.data = uri
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                startActivity(intent)
            } catch (exp: Exception) {
                exp.printStackTrace()
            }
        } else {
            intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(file.toString()), "application/pdf")
            intent = Intent.createChooser(intent, "Open File")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}

