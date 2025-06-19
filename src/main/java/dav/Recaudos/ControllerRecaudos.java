package dav.Recaudos;

import dav.transversal.DatosDavivienda;
import dav.transversal.MovimientoStratus;
import dav.transversal.Stratus;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.DataDriven;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

import java.util.List;

import dav.FiduciaEstrusturada.PageFiduciaFront;
import dav.Recaudos.*;
import dav.pymes.PageLoginPymes;
import dav.pymes.moduloCrearTx.ControllerDestinosMasivos;

public class ControllerRecaudos {

	PageRecaudos pageRecaudos = null;
	PageLoginPymes pageLogPymes = null;
	int eventStatus = 0;
	static String mensajeReport = null;
	ControllerDestinosMasivos controllerDestinosMasivos = null;

	public ControllerRecaudos(PageLoginPymes pageParent) {
		pageLogPymes = pageParent;
	}
//Capturados de Mensajes y reportes del escenario
	public void ConsultaConvenio() throws Exception {
		String CodigoConv = SettingsRun.getTestData().getParameter("Codigo Convenio Empresa").trim();
		String BaseD = SettingsRun.getTestData().getParameter("Base de Datos").trim();
		String NomEMP = SettingsRun.getTestData().getParameter("Nombre de la empresa para publicar").trim();
		String Estructura = SettingsRun.getTestData().getParameter("Estructura").trim();
		String[] arrayDatosConv = { "NUMERO ID", "NOMBRE CONV", "CONVENIO", "Referencia2", "Referencia1" };
		String[] datosconvenio = DatosDavivienda.STRATUS.consultarConvenio(CodigoConv, arrayDatosConv);
//		for (String dato : datosconvenio) {
//			System.out.println(dato);
//		}
		if (datosconvenio[2].equals(CodigoConv)) {
			Reporter.reportEvent(Reporter.MIC_PASS, "Numero de convenio coincide en stratus");
		} else if (!datosconvenio[2].equals(CodigoConv)) {
			Reporter.reportEvent(Reporter.MIC_WARNING, "Numero de convenio NO coincide en stratus");
		}
		if (datosconvenio[1].equals(NomEMP)) {
			Reporter.reportEvent(Reporter.MIC_PASS, "Nombre de convenio coincide en stratus");
		} else if (!datosconvenio[1].equals(NomEMP)) {
			Reporter.reportEvent(Reporter.MIC_WARNING, "Nombre de convenio NO coincide en stratus");
		}
		if (datosconvenio[3].contains("1")) {
			if (Estructura.equals("Excel 2 Referencia")) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Numero de referencias de convenio coincide en stratus");
			} else {
				Reporter.reportEvent(Reporter.MIC_WARNING, "Numero de referencias no corresponden al convenio en stratus");
			}
		} else if (datosconvenio[3].contains("")) {
			if (Estructura.equals("Excel 1 Referencia")) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Numero de referencias de convenio coincide en stratus");
			} else {
				Reporter.reportEvent(Reporter.MIC_WARNING, "Numero de referencias no corresponden al convenio en stratus");
			}
		}
	}
	//Capturados de Mensajes y reportes del escenario
	public void NuevasBD() throws Exception {
		this.pageRecaudos = new PageRecaudos(pageLogPymes);
		boolean Resultadop = this.pageRecaudos.RecaudosIr();
		if (Resultadop) {
			eventStatus = Reporter.MIC_PASS;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);
		} else {
			eventStatus = Reporter.MIC_FAIL;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);

		}
		boolean ResultadoRef = this.pageRecaudos.NuevasBD();
		if (ResultadoRef) {
			eventStatus = Reporter.MIC_PASS;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);
		} else {
			eventStatus = Reporter.MIC_FAIL;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);

		}
	}
	//Capturados de Mensajes y reportes del escenario
	public void ConsultaReferencias() throws Exception {
		this.pageRecaudos = new PageRecaudos(pageLogPymes);
		boolean Resultadop = this.pageRecaudos.RecaudosIr();
		if (Resultadop) {
			eventStatus = Reporter.MIC_PASS;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);
		} else {
			eventStatus = Reporter.MIC_FAIL;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);

		}
		boolean ResultadoConsulta = this.pageRecaudos.ConsultaReferencias();
		if (ResultadoConsulta) {
			eventStatus = Reporter.MIC_PASS;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);
		} else {
			eventStatus = Reporter.MIC_FAIL;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);

		}
	}
	//Capturados de Mensajes y reportes del escenario
	public void AprobacionBaseDatos() throws Exception {
		this.pageRecaudos = new PageRecaudos(pageLogPymes);
		boolean Resultadop = this.pageRecaudos.RecaudosIr();
		if (Resultadop) {
			eventStatus = Reporter.MIC_PASS;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);
		} else {
			eventStatus = Reporter.MIC_FAIL;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);

		}
		boolean ResultadoAprobacion = this.pageRecaudos.AprobacionBaseDatos();
		if (ResultadoAprobacion) {
			eventStatus = Reporter.MIC_PASS;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);
		} else {
			eventStatus = Reporter.MIC_FAIL;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);

		}
	}
	//Capturados de Mensajes y reportes del escenario
	public void ConsultaProcesos() throws Exception {
		this.pageRecaudos = new PageRecaudos(pageLogPymes);
		boolean Resultadop = this.pageRecaudos.RecaudosIr();
		if (Resultadop) {
			eventStatus = Reporter.MIC_PASS;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);
		} else {
			eventStatus = Reporter.MIC_FAIL;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);

		}
		boolean ResultadoConsulta = this.pageRecaudos.ConsultaProcesos();
		if (ResultadoConsulta) {
			eventStatus = Reporter.MIC_PASS;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);
		} else {
			eventStatus = Reporter.MIC_FAIL;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);

		}
		
		
	}
	//Capturados de Mensajes y reportes del escenario
	public void NovedadesBD() throws Exception {
		this.pageRecaudos = new PageRecaudos(pageLogPymes);
		boolean Resultadop = this.pageRecaudos.RecaudosIr();
		if (Resultadop) {
			eventStatus = Reporter.MIC_PASS;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);
		} else {
			eventStatus = Reporter.MIC_FAIL;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);

		}
		boolean ResultadoNovedad = this.pageRecaudos.NovedadesBD();
		if (ResultadoNovedad) {
			eventStatus = Reporter.MIC_PASS;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);
		} else {
			eventStatus = Reporter.MIC_FAIL;
			mensajeReport = this.pageRecaudos.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReport);
//			SettingsRun.exitTestIteration();
		}
		
	}

	
	
	

}
