package dav.Informes;

import dav.middlePymes.PageInicioMiddle;
import dav.pymes.PageLoginPymes;
import dav.pymes.moduloCrearTx.PageOrigen;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class ControllerInformes {

// ====================================[Page & Crontroller]===================================================================================

	PageLoginPymes pageLoginPymes = null;
	PageLoginPymes pageLogin = null;
	PageInicioMiddle inicioMiddle = null;
	PageOrigen pageOrigen = null;
	PageInformes pageInformes = null;

// ===========================================================================================================================================

	/**
	 * Pages y constructores
	 */
	public ControllerInformes(PageLoginPymes pageParent) {
		pageLoginPymes = pageParent;
		this.inicioMiddle = new PageInicioMiddle(pageParent);
		this.pageOrigen = new PageOrigen(pageLoginPymes);

	}


// ===========================================================================================================================================

	public void informesMiddle() throws Exception {
		String mensajeReporte = "";
		int eventStatus = Reporter.MIC_PASS;

		boolean resultadoPrueba = this.validacionInformes();
		if (resultadoPrueba) {
			mensajeReporte = this.pageInformes.getMensajes();
			Reporter.reportEvent(eventStatus, mensajeReporte);

		} else {
			eventStatus = Reporter.MIC_FAIL;
			mensajeReporte = this.pageInformes.getMensajes();
			this.pageOrigen.terminarIteracion(eventStatus, mensajeReporte);
		}

	}

	public boolean validacionInformes() throws Exception {

		boolean resultado = false;

		String tipoPrueba = SettingsRun.getTestData().getParameter("Tipo de Prueba").trim();
		
		Reporter.write("================INFORMES - Tipo de prueba ["+tipoPrueba+"]================");
		Reporter.write("");
		Reporter.write("Se ingresa al módulo y se ingresa párametros de búsqueda");
		
		String[] ruta = this.RutaInforme(tipoPrueba);
		if (ruta[2] == null) {
			String msgError = this.inicioMiddle.irAOpcion(ruta[0], "Informes", ruta[1]);
			if (msgError != null) {
			 this.pageOrigen.terminarIteracion(Reporter.MIC_FAIL, msgError);
			}
		} else {
			String msgError = this.inicioMiddle.irAOpcion(ruta[0], "Informes", ruta[1], ruta[2]);
			if (msgError != null) {
				this.pageOrigen.terminarIteracion(Reporter.MIC_FAIL, msgError);
			}
		}

		switch (tipoPrueba) {

		case "Acceso al Sistema":
			this.pageInformes = new PageInformes(this.pageLoginPymes);
			resultado = this.pageInformes.accesoAlSistema();
			break;
		case "Actividades Administradores":
			this.pageInformes = new PageInformes(this.pageLoginPymes);
			resultado = this.pageInformes.actividadesAdministradores();
			break;
		case "Cobros Efectivos":
			this.pageInformes = new PageInformes(this.pageLoginPymes);
			resultado = this.pageInformes.cobrosEfectivos();
			break;
		case "Informe de Empresas":
			this.pageInformes = new PageInformes(this.pageLoginPymes);
			resultado = this.pageInformes.informeEmpresas();
			break;
		case "Estadísticas de Transacciones":
			this.pageInformes = new PageInformes(this.pageLoginPymes);
			resultado = this.pageInformes.estádisticasTransacciones();
			break;
		case "Informe Operativo":
			this.pageInformes = new PageInformes(this.pageLoginPymes);
			resultado = this.pageInformes.informeOperativo();
			break;
		case "Pagos Masivos":
			this.pageInformes = new PageInformes(this.pageLoginPymes);
			resultado = this.pageInformes.pagosMasivos();
			break;
		case "Pagos y Transferencias":
			this.pageInformes = new PageInformes(this.pageLoginPymes);
			resultado = this.pageInformes.pagosYTransferencias();
			break;
		case "Servicios Contratados":
			this.pageInformes = new PageInformes(this.pageLoginPymes);
			resultado = this.pageInformes.serviciosContratados();
			break;
		case "Transacciones ACH":
			this.pageInformes = new PageInformes(this.pageLoginPymes);
			resultado = this.pageInformes.transaccionesACH();
			break;
		case "Transacciones Realizadas":
			this.pageInformes = new PageInformes(this.pageLoginPymes);
			resultado = this.pageInformes.transaccionesRealizadas();
			break;
		case "Usuarios Creados":
			this.pageInformes = new PageInformes(this.pageLoginPymes);
			resultado = this.pageInformes.usuariosCreados();
			break;
		case "Aprobaciones App":
			this.pageInformes = new PageInformes(this.pageLoginPymes);
			resultado = this.pageInformes.aprobacionesApp();
			break;
		case "Informes Autogestión":
			this.pageInformes = new PageInformes(this.pageLoginPymes);
			resultado = this.pageInformes.informesAutogestion();
			break;
		case "Retiros sin tarjeta":
			this.pageInformes = new PageInformes(this.pageLoginPymes);
			resultado = this.pageInformes.informeRetirosSinTarjeta();
			break;
		case "Negocios Fiduciarios":
			this.pageInformes = new PageInformes(this.pageLoginPymes);
			resultado = this.pageInformes.negociosFiduciarios();
			break;
		case "Transacciones FIC":
			this.pageInformes = new PageInformes(this.pageLoginPymes);
			resultado = this.pageInformes.informeTransaccionesFic();
			break;
		case "Estados Empresa":
			this.pageInformes = new PageInformes(this.pageLoginPymes);
			resultado = this.pageInformes.informeEstadosEmpresa();
			break;
		case "Informes Transferencias Internacionales":
			this.pageInformes = new PageInformes(this.pageLoginPymes);
			resultado = this.pageInformes.informeEstadosEmpresa();
			break;	
		
			
			

		default:
			break;
		}
		return resultado;
	}

	/**
	 * Retorna la ruta de cada informe con su respectivo titulo y si tienen su
	 * modulos [0] Titulo [1]subModulo [2]Submodulo2
	 * 
	 * @param tipoconsul
	 * @return
	 */
	public String[] RutaInforme(String tipoconsul) {
		String[] ruta = null;

		if (tipoconsul.contains("Acceso al Sistema")) {
			ruta = new String[] { "Consulta de Acceso al Sistema", "Acceso al Sistema", null };
		} else if (tipoconsul.contains("Actividades Administradores")) {
			ruta = new String[] { "Informe Consultas Novedades de Administración", "Actividades Administradores",
					null };
		} else if (tipoconsul.contains("Cobros Efectivos")) {
			ruta = new String[] { "Informes de Cobros Efectivos", "Cobros Efectivos", null };
		} else if (tipoconsul.contains("Informe de empresas")) {
			ruta = new String[] { "Estadísticas Empresas Portal Pyme", "Informe de empresas", null };
		} else if (tipoconsul.contains("Estadísticas de Transacciones")) {
			ruta = new String[] { "Estadísticas Portal Empresarial", "Estadísticas de Transacciones", null };
		} else if (tipoconsul.contains("Estadísticas de Transacciones")) {
			ruta = new String[] { "Informe Operativo", "Estadísticas de Transacciones", null };
		} else if (tipoconsul.contains("Informe Operativo")) {
			ruta = new String[] { "Informe Operativo", "Informe Operativo", null };
		} else if (tipoconsul.contains("Pagos Masivos")) {
			ruta = new String[] { "Informes Pagos Masivos", "Pagos Masivos", null };
		} else if (tipoconsul.contains("Pagos y Transferencias")) {
			ruta = new String[] { "Informes Pagos y Transferencias", "Pagos y Transferencias", null };
		} else if (tipoconsul.contains("Servicios Contratados")) {
			ruta = new String[] { "Consulta Servicios Contratados", "Servicios Contratados", null };
		} else if (tipoconsul.contains("Transacciones ACH")) {
			ruta = new String[] { null, "Transacciones ACH", null };
		} else if (tipoconsul.contains("Transacciones Realizadas")) {
			ruta = new String[] { null, "Transacciones Realizadas", null };
		} else if (tipoconsul.contains("Usuarios Creados")) {
			ruta = new String[] { null, "Usuarios Creados", null };
		} else if (tipoconsul.contains("Multiabonos Consulta dispersión")) {
			ruta = new String[] { null, "Multiabonos", "Consulta dispersión" };
		} else if (tipoconsul.contains("Multiabonos Consulta Parametros")) {
			ruta = new String[] { null, "Multiabonos", "Consulta Parametros" };
		} else if (tipoconsul.contains("Aprobaciones App")) {
			ruta = new String[] { null, "Aprobaciones App", null };
		} else if (tipoconsul.contains("Informes Autogestión")) {
			ruta = new String[] { null, "Informes Autogestión", null };
		} else if (tipoconsul.contains("Retiros sin tarjeta")) {
			ruta = new String[] { null, "Retiros sin tarjeta", null };
		} else if (tipoconsul.contains("Negocios Fiduciarios")) {
			ruta = new String[] { null, "Negocios Fiduciarios", null };
		} else if (tipoconsul.contains("Transacciones FIC")) {
			ruta = new String[] { null, "Transacciones FIC", null };
		} else if (tipoconsul.contains("Estados Empresa")) {
			ruta = new String[] { null, "Estados Empresa", null };
		} else if (tipoconsul.contains("Informes Transferencias Internacionales")) {
			ruta = new String[] { null, "Informes Transferencias Internacionales", null };
		} else if (tipoconsul.contains("Administración del efectivo")) {
			ruta = new String[] { null, "Administración del efectivo", null };
		} else if (tipoconsul.contains("Administre sus Créditos")) {
			ruta = new String[] { null, "Administre sus Créditos", null };
		}

		return ruta;
	}

}
