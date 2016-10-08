package mx.uaemex

import grails.transaction.Transactional
import org.springframework.web.context.request.RequestContextHolder
   
import net.sf.jasperreports.engine.JRException
import net.sf.jasperreports.engine.JRExporter
import net.sf.jasperreports.engine.JRExporterParameter
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.engine.JasperExportManager
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource

@Transactional
class DocumentExportService {

	def assetResourceLocator

	def exportDocumentPDF() {
		
		URL logoReportURL = this.class.classLoader.getResource('UAEMex.png')
		URL reportJasperURL = this.class.classLoader.getResource('report_bitacora.jrxml')
		String fileNamePDF = "Bitacora_${new Date().format('dd_MMMMM_yyyy')}.pdf"

		//TODO: Obtiene el objeto response desde esta capa de servicio, aunque se debe delegar este proceso al Controller.
		def response = RequestContextHolder.currentRequestAttributes().response
		response.setContentType("application/pdf")
		response.setHeader("Content-Disposition", "Attachment;Filename=\"${fileNamePDF}\"")

		try {
			String reportJRXML = reportJasperURL.file.toURI()

			// Compilar reporte jasper
			String reportJasper = JasperCompileManager.compileReportToFile(reportJRXML)
			
			// Parametros del reporte
			Map<String,String> reportParam = new HashMap<String,String>()
			reportParam	<<	["LOGO_PDF": logoReportURL.toURI()]

			List data = []
			List bitacoraList = Bitacora.list()
			bitacoraList.each{row-> 
				data    <<  ["bitacora_id": row.id ?: "", "bitacora_date_created": row.dateCreated ?: "", "bitacora_document_id": row.document?.fullPath ?: "",
				"bitacora_group_name": row.groupName ?: "", "bitacora_laboratory_id": row.laboratory?.name ?: "", 
				"bitacora_career_id": row.career?.name ?: "", "bitacora_teacher_id": "${row.teacher?.name} ${row.teacher?.lastName}" ?: "",
				"bitacora_user_id": "${row.user?.name} ${row.user?.lastName}" ?: ""
				]
			}
			
			// Llenar reporte 
			JasperPrint jasperPrint = JasperFillManager.fillReport(reportJasper, reportParam, new JRBeanCollectionDataSource(data))

			JRExporter exporter = new JRPdfExporter()
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint)
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.outputStream)
			exporter.exportReport()

			//Generar reporte en ubicacion establecida
			//JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\AMD\\Documents\\jasper\\")

		} catch (Exception e) {
				throw new RuntimeException("No se puede generar el reporte", e)
		} finally {
			response.outputStream.flush()
			response.outputStream.close()
		}
  }

}
