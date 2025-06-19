package dav.Informes;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.IFactoryAnnotation;
import org.yaml.snakeyaml.events.Event.ID;

import dav.AutogestionServicios.PageAutogestionServicioMiddle;
import dav.middlePymes.PageInicioMiddle;
import dav.pymes.PageLoginPymes;
import dav.pymes.moduloCrearTx.PageOrigen;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageInformes extends BasePageWeb {
	/**
	 * Page, contructores, variables generales
	 * 
	 */
	PageLoginPymes pageLogin = null;
	PageInicioMiddle inicioMiddle = null;

	static String mensajes = null;
	static int cont = 0;
	static String nombreUsuario = null;
	static String numeroIdentificacionUsu = null;
	static String tipoIdentificacionUsu = null;
	static String clienteEmpresarial = null;
	static String fechaNovedadDesde = null;
	static String fechaNovedadHasta = null;
	static String servicios = null;
	static String tipoAutenticación = null;
	static String guardarArchivo = null;
	static String FechaHoraNovedad = null;
	static String nombreEmpresa = null;
	static String tipoIdentifEmp = null;
	static String numeroIDEmpresa = null;
	static String estado = null;

	public PageInformes(BasePageWeb parentPage) throws Exception {
		super(parentPage);
		this.pageLogin = new PageLoginPymes(this);

		// variables de uso general archivo de lanzamiento
		this.nombreUsuario = SettingsRun.getTestData().getParameter("Nombre de usuario");
		this.numeroIdentificacionUsu = SettingsRun.getTestData().getParameter("Número Identificación");
		this.tipoIdentificacionUsu = SettingsRun.getTestData().getParameter("Tipo Identificación");
		this.clienteEmpresarial = SettingsRun.getTestData().getParameter("Cliente Empresarial");
		this.fechaNovedadDesde = SettingsRun.getTestData().getParameter("Fecha Desde");
		this.fechaNovedadHasta = SettingsRun.getTestData().getParameter("Fecha Hasta");
		this.servicios = SettingsRun.getTestData().getParameter("Servicios");
		this.tipoAutenticación = SettingsRun.getTestData().getParameter("Tipo de autenticación");
		this.guardarArchivo = SettingsRun.getTestData().getParameter("Guardar Archivo");
		this.FechaHoraNovedad = SettingsRun.getTestData().getParameter("Búsqueda Fecha/Hora");
		this.nombreEmpresa = SettingsRun.getTestData().getParameter("Nombre Empresa");
		this.tipoIdentifEmp = SettingsRun.getTestData().getParameter("Tipo ID Empresa");
		this.numeroIDEmpresa = SettingsRun.getTestData().getParameter("Numero ID Empresa");
		this.estado = SettingsRun.getTestData().getParameter("Estado");

	}

	// Locators generales Informes
	
	By moduloInformesMiddle = By.xpath("//span[@id='lblMasterMenu']//a[contains(text(), 'Informes')]//parent::td");
	By msjAlerta = By.id("lblMasterAlerta");
	By listServicios = By.id("cphCuerpo_ddlServicios");
	// se presenta al dar clic en ver detalle
	
	By btnRegresar = By.id("cphCuerpo_btRegresar");
	By btnVolver = By.id("cphCuerpo_btVolver");
	
	// Locators que funcionan para informe acceso al sistema, actividades
	// administradores, informe de empresas, Estadísticas de Transacciones, informe
	// operativo, servicios contradados
	
	By inputFechaNovedadDesde = By.id("cphCuerpo_txtFechaInicial");
	By inputFechaNovedadHasta = By.id("cphCuerpo_txtFechaFinal");
	By btnBuscar = By.id("cphCuerpo_btBuscar");
	String verDetalleID = "//*[contains(text(),'FECHA-HORA')]/parent::tr/td/a[contains(text(),'Ver detalle')]";
	By btnSiguiente = By.xpath("//*[@id='cphCuerpo_lnkSiguiente'][not(contains(@class, 'aspNetDisabled'))]");
	By btnSiguienteaspNable = By.xpath("//*[@id='cphCuerpo_lnkSiguiente'][(contains(@class, 'aspNetDisabled'))]");
	By inputClienteEmpresarial = By.id("cphCuerpo_txtClienteEmpresarial");
	By btnCalendarInicial = By.id("cphCuerpo_btnCalentario1");
	By btnCalendarHasta = By.id("cphCuerpo_btnCalentario2");
	// funciona para informe de empresas e informe de Pagos masivos
	
	By listPerfilUsuario = By.id("cphCuerpo_ddlPerfilUsuario");

	// imprimir y descargar tabla de resultados
	
	By btnImprimirLista = By.xpath("//*[@value='Imprimir Lista']");
	By btnGuardar = By.xpath("//*[@value='Guardar']");
	By btnImprimirImpresora = By.id("idPrint");
	By btnSalir = By.xpath("//*[@id='form1']/table[1]/tbody/tr/td[3]/a");
	By btnImprimirListaOpcion2 = By.xpath("//*[@value='Imprimir lista']");
	By btnImprimirListaOpcion3 = By.id("cphCuerpo_btImprimir");

	// Locators Informe Acceso al sistema
	
	By inputNombreUsuario = By.id("cphCuerpo_txtNombreUsuario");
	By listTipoIdentUsuario = By.id("cphCuerpo_ddlTipoIdentificacionUsuario");
	By inputNumIdentifUsuario = By.id("cphCuerpo_txtIdentificacionUsuario");
	By listCanal = By.id("cphCuerpo_ddlcanales"); 
	By listTipoAutenticacion = By.id("cphCuerpo_ddlTipoAutenticacion"); // funciona para pagos masivos, informe de pagos y transferencias, informe usuarios creados
	By tablaResultados = By.xpath("//div[@id='cphCuerpo_pnlGrilla']//parent::*/div/table/tbody");

	// Locators Informe Actividades Administradores
	
	By listTipoNovedad = By.id("cphCuerpo_ddlTipoNovedad");
	By listAplicativo = By.id("cphCuerpo_ddlAplicativo");
	By listTipoIdentAdmin = By.id("cphCuerpo_ddlTipoIdentificacion"); // funciona para modulo servicios contratados
	By inputNumIdent = By.id("cphCuerpo_txtNumID");
	By tablaResultadosActAdmin = By.id("cphCuerpo_gvActividadesAdministradores");

	// Locators Informe Cobros Efectivos
	
	By listTipoIdentifEmp = By.id("cphCuerpo_ddlTipoIdentificacionEmpresa"); // funciona para pagos masivos, usuarios creados, transacciones FIC
	By inputNumIdentifEmp = By.id("cphCuerpo_txtNumIdentificacionEmpresa"); // funciona para pagos masivos, usuarios creados, autogestión, retiros sin tarjeta
	By inputNombreEmpCobros = By.id("cphCuerpo_txtNombreEmpresa"); // funciona para pagos masivos, servicios contratados, retiros sin tarjeta
	By inputFechaNovedadDesdeCobros = By.id("cphCuerpo_txtFechaDesde");
	By inputFechaNovedadHastaCobros = By.id("cphCuerpo_txtFechaHasta");
	By tablaResultadosCobros = By.id("cphCuerpo_gvCobrosEfectivos");
	String verDetalleCobros = "//*[contains(text(),'FECHA-HORA')]/parent::tr/td/a[contains(text(),'Ver Detalle')]";
	By btnGuardarCobros = By.id("cphCuerpo_btGuardarDet");

	// Locators Informe de Empresas

	By listEstado = By.id("cphCuerpo_ddlEstados");
	By listCombosdeServicio = By.id("cphCuerpo_ddlCombos");
	By listVentaDigital = By.id("cphCuerpo_ddlVentaDigital");
	By tablaResultadosEmpresas = By.id("cphCuerpo_gvEmpresa");
	By columnaTablaResultFechaCancelacion = By.xpath("//*[@id=\"cphCuerpo_gvEmpresa\"]/tbody/tr[1]/th[23]");
	By columnaTablaResultTeléfonoAdmin = By.xpath("//*[@id='cphCuerpo_gvEmpresa']/tbody/tr[1]/th[15]");

	// locators Informe Estádisticas de transacciones
	
	By tipoIdentificacionEmp = By.id("cphCuerpo_dropTipoIdentificacion");
	By numIdentEmp = By.id("cphCuerpo_txtNumIdentificacion"); // funciona para servicios contratados
	By tablaResultadosEstadisticasPortal = By.id("cphCuerpo_gvEstadisticasTransacciones");
	
	// locators de nombres de columnas de la tabla de resultados
	
	By ProvAchValor = By.xpath("//*[@id=\"cphCuerpo_gvEstadisticasTransacciones\"]/tbody/tr/th[30]");
	By LibafcValor = By.xpath("//*[@id=\"cphCuerpo_gvEstadisticasTransacciones\"]/tbody/tr/th[43]");
	By trccDestinosCorresponsalValor = By.xpath("//*[@id=\"cphCuerpo_gvEstadisticasTransacciones\"]/tbody/tr/th[56]");
	By trccQrValor = By.xpath("//*[@id=\"cphCuerpo_gvEstadisticasTransacciones\"]/tbody/tr/th[62]");
	By btnImprimirListaEstadisticas = By.xpath("//*[@value='Imprimir Lista ']");
	
	// Locators Informe operativo
	By tablaResultadosOperativo = By.id("cphCuerpo_gvInformeOperativo");
	String verDetalleOperativo = "//*[contains(text(),'FECHA-HORA')]/parent::tr/td/a[contains(text(),'Detalle')]";
	By  filasTablaResultadosOperativo = By.xpath("//*[@id='cphCuerpo_gvInformeOperativo']//tbody//tr");
	String filasTablaResultadosString = "//*[@id='cphCuerpo_gvInformeOperativo']";
	
	
	// Locators Informe Pagos Masivos
	By listFecha = By.id("cphCuerpo_ddlFecha");
	By inputNroProceso = By.id("cphCuerpo_txtProceso"); // funciona para informe retiros sin tarjeta
	By inputNombreProceso = By.id("cphCuerpo_txtNombreProceso");
	By listProductoOrigen = By.id("cphCuerpo_ddlProductoOrigen");
	By listEstados = By.id("cphCuerpo_ddlEstado"); // funciona para retiros sin tarjeta, transacciones FIC
	By tablaResultadoPagosMasiv = By.id("cphCuerpo_gvPagosMasivos");
	String armarXpathNumeroProceso = "//*[contains(text(),'NUMERO_PROCESO')]";
	
	
	// Locators Informe Pagos y Transferencias
	
	By inputNumCuentaOrigen = By.id("cphCuerpo_txtCuentaOrigen");
	By inputNumCuentaDestino = By.id("cphCuerpo_txtCuentaDestino");
	By listPagosQR = By.id("cphCuerpo_dpPagoQR");
	By listServiciosPagos = By.id("cphCuerpo_ddlServicio");
	By tablaResultadosPagosYTransf = By.xpath("//*[@id=\"cphCuerpo_gvPagosTransferencias\"]");
	By tablaResultadosColumnaServicio = By.xpath("//*[@id=\"cphCuerpo_gvPagosTransferencias\"]/tbody/tr/th[16]");
	String buscarPagoTransf = "//*[contains(text(),'FECHA')]/following-sibling::td[contains(text(),'HORA')]";
	
	// Locators Servicios Contratados
	
	By btnFechaCreacionDesde = By.id("cphCuerpo_btnCalentario3");
	By btnFechaCreacionHasta = By.id("cphCuerpo_Button1"); // funciona para usuarios creados
	By btnFechaModificacionDesde = By.id("cphCuerpo_Button2");
	By btnFechaModificacionHasta = By.id("cphCuerpo_Button3");
	By inputNumIdentif = By.id("cphCuerpo_txtNumIdentificacion");
	// tabla resultados busqueda 
	By tablaResultadosServicios = By.id("cphCuerpo_gvServicios");
		// titulos de nombres de columnas de la tabla de resultados
	By tablaResultDescSobreRangoDestinoTarjPrep = By.xpath("//*[@id='cphCuerpo_gvServicios']/tbody/tr/th[21]");
	By tablaResultNroTransaccRangoSuperior = By.xpath("//*[@id='cphCuerpo_gvServicios']/tbody/tr/th[29]");
	By tablaResultTarifaEspecial = By.xpath("//*[@id='cphCuerpo_gvServicios']/tbody/tr/th[34]");
	
	// Locators Transacciones ACH
	
	By btnfechaInicialConsulta = By.id("cphCuerpo_btnCalentario1");
	By btnfechaFinalConsulta = By.id("cphCuerpo_btnCalentario2");
	By tablaResultadosTransACH = By.id("cphCuerpo_gvUsuario");
	By tablaResultadosColumnaBanco = By.xpath("//*[@id=\"cphCuerpo_gvUsuario\"]/tbody/tr/th[14]");
	
	// Locators Transacciones Realizadas
	
	By tablaResultTransRealizadas = By.id("cphCuerpo_gvEstadisticasTransacciones");
	
	// Locators Usuarios Creados
	
	By tablaResultUsuariosCreados = By.id("cphCuerpo_gvUsuario");
	By nombreColumnaTablaNombreEmp = By.xpath("//*[@id=\"cphCuerpo_gvUsuario\"]/tbody/tr/th[15]");
	
	//Locators Informe Aprobaciones 
	By listEstadoAprobacion = By.id("cphCuerpo_dropEstado");
	By perfilUsuarioAprob = By.id("cphCuerpo_dropPerfilUsuario");
	By productoOrigenAprob = By.id("cphCuerpo_dropProductoOrigen");
	By listTipoIdentificacionEmp = By.id("cphCuerpo_dropTipoIdentificacionEmpresa"); // funciona para informe autogestion
	By inputClienteEmpre = By.id("cphCuerpo_txtNumClienteEmpresarial"); // funciona para informe autogestion
	
	// Locators Informe Autogestión 
	
	By listNovedadAutoGest = By.id("cphCuerpo_dropNovedad");
	By listTipoIdentifUsu = By.id("cphCuerpo_dropTipoIdentificacionUsuario");
	By inputNumIdentiUsuario = By.id("cphCuerpo_txtNumIdentificacionUsuario");
	By listCanalAutoges = By.id("cphCuerpo_dropCanal");

	// Locators Informe Retiros sin tarjeta
	
	By inputCuentaOrig = By.xpath("//*[@id='cphCuerpo_txtCuenta']");
	By metodoAprobacion = By.id("cphCuerpo_ddlMetodoDeAprobacion");
	By listCanalRetiro = By.id("cphCuerpo_ddlCanalRetiro");
	By listCanalR= By.id("cphCuerpo_ddlCanal");
	
	// Locators Negocios Fiduciarios 
	By iFrameNegociosFiduciarios = By.id("cphCuerpo_IframeFiducia");
	By btnInformeActividades = By.xpath("//*[contains(text(),'Informe de Actividades')]");
	By listNovedad = By.id("NoveltySelected");
	By inputNombreUsu = By.id("UserName");
	By inputFechaNovDesde = By.xpath("//*[@id=\"StartDate\"]");
	By inputFechaNovHasta = By.id("EndDate");
	By listTipoIdent = By.id("IdentificationTypeSelected");
	By inputNumeroIdentifi = By.id("IdentificationNumber");
	By listModulo = By.id("ModuleSelected");
	By btnBuscarNegocios = By.id("search");
	
	// Locators Transacciones FIC
	
	By fechaConsultaDesde = By.id("cphCuerpo_btnCalentarioDesde");
	By fechaConsultaHasta = By.id("cphCuerpo_btnCalentarioHasta");
	By listTipoTX = By.id("cphCuerpo_ddlTipoTX");
	
	// Locators Estados Empresa
	
	By numberIdEmp = By.id("cphCuerpo_txtnumeroIdEmpresarial");
	By clientEmp = By.xpath("//*[@id='cphCuerpo_txtclienteEmpresarial']");
	
	// Locators Transferencias Internacionales 
	
	By iFrameTransfInternacional = By.id("cphCuerpo_IframeDivisas");
	
	// =======================================================================================================================
	public boolean accesoAlSistema() throws Exception {

		String canal = SettingsRun.getTestData().getParameter("Canal");

		boolean resultadoPrueba = false;

		cont = 0;
		do {
			cont++;
			DXCUtil.wait(2);
		} while (this.element(inputFechaNovedadDesde) == null && cont < 4);
		// parametros de busqueda, se diligencian en middle solo los campos que se
		// ingresaron en el archivo de lanzamiento
		if (nombreUsuario != "") {
			this.write(inputNombreUsuario, nombreUsuario);
		}
		if (tipoIdentificacionUsu != "") {
			this.selectListItemExacto(listTipoIdentUsuario, tipoIdentificacionUsu);
		}
		if (numeroIdentificacionUsu != "") {
			this.write(inputNumIdentifUsuario, numeroIdentificacionUsu);
		}

		if (clienteEmpresarial != "") {
			this.write(inputClienteEmpresarial, clienteEmpresarial);
		}
		if (fechaNovedadDesde != "") {
			this.write(inputFechaNovedadDesde, fechaNovedadDesde);
		}
		if (fechaNovedadHasta != "") {
			this.write(inputFechaNovedadHasta, fechaNovedadHasta);
		}
		if (canal != "") {
			this.selectListItem(listCanal, canal);
		}
		if (servicios != "") {
			DXCUtil.wait(2);
			this.selectListItemExacto(listServicios, servicios);
		}
		if (tipoAutenticación != "") {
			this.selectListItem(listTipoAutenticacion, tipoAutenticación);
		}

		this.click(btnBuscar);
		cont = 0;

		do {
			cont++;
			DXCUtil.wait(1);
		} while (this.element(tablaResultados) == null && this.element(msjAlerta) == null && cont < 7);

		if (this.element(msjAlerta) != null) {
			mensajes = "Se visualiza mensaje " + "[" + this.getText(msjAlerta) + "]";
			Evidence.save("Mensaje de alerta");
			return resultadoPrueba;
		}
		if (this.element(tablaResultados) == null) {
			mensajes = "No se encontraron resultados con los parámetros de búsqueda dados.";
			Evidence.saveFullPage("No se encontro resultados de búsqueda", this);
			return resultadoPrueba;
		}
		Evidence.saveFullPage("Resultados búsqueda", this);
		Reporter.reportEvent(Reporter.MIC_INFO, "Se encontraron resultados con los parámetros de búsqueda dados.");

		String cambiarIDVerDetalle = "";
		WebElement verDetalle = null;
		cambiarIDVerDetalle = verDetalleID.replace("FECHA-HORA", FechaHoraNovedad);

		// Se da clic en el texto ver detalle que coincida con la fecha-hora ingresa en
		// la columna FechaHoraNovedad en el excel
		do {
			verDetalle = this.element((By.xpath(cambiarIDVerDetalle)));
			if (this.element(verDetalle) == null) {
				if (this.element(btnSiguiente) == null) {
					break;
				}
				this.click(btnSiguiente);
				DXCUtil.wait(3);
				Evidence.saveFullPage("tabla resultados", this);

			} else {
				this.click(verDetalle);
				cont = 0;	
				do {
					cont++;
					DXCUtil.wait(1);
				} while (this.element(btnRegresar) == null && cont < 6);
				Evidence.saveFullPage("Consulta de Acceso al Sistema", this);
				this.click(btnRegresar);
				cont = 0;
				do {
					cont++;
					DXCUtil.wait(1);
				} while (this.element(tablaResultados) == null && cont < 6);
				mensajes = "Se encontró resultado de consulta de acceso al sistema con fecha-Hora " + "["
						+ FechaHoraNovedad + "] de manera exitosa.";
				resultadoPrueba = true;
				break;
			}
		} while (this.element(verDetalle) == null);

		if (resultadoPrueba == false) {
			mensajes = "No se encuentró ningún registro que coincida con la fecha-Hora " + "[" + FechaHoraNovedad + "]";
		}

		// se descarga archivo con resultados obtenidos
		if (guardarArchivo.contentEquals("Imprimir")) {
			imprimirArchivos(btnImprimirLista, btnImprimirImpresora, btnSalir, "Acceso al Sistema");

		} else if (guardarArchivo.contentEquals("Guardar")) {
			if (this.element(btnGuardar) != null) {
				this.click(btnGuardar);
				DXCUtil.wait(6);
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Se descarga el archivo de consulta acceso al sistema correctamente.");
			} else {
				Reporter.reportEvent(Reporter.MIC_WARNING,
						"No permitio descargar archivo de acceso al sistema, no se encontró el botón guardar.");
			}
		} else if (guardarArchivo.contentEquals("Imprimir y Guardar")) {
			imprimirArchivos(btnImprimirLista, btnImprimirImpresora, btnSalir, "Acceso al Sistema");

			if (this.element(btnGuardar) != null) {
				this.click(btnGuardar);
				DXCUtil.wait(6);
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Se descarga el archivo de consulta acceso al sistema correctamente.");
			} else {
				Reporter.reportEvent(Reporter.MIC_WARNING,
						"No permitio descargar archivo de acceso al sistema, no se encontró el botón guardar.");
			}
		}

		return resultadoPrueba;
	}

	// =======================================================================================================================

	public boolean actividadesAdministradores() throws Exception {
		boolean resultadoPrueba = false;

		String tipoNovedad = SettingsRun.getTestData().getParameter("Tipo Novedad");
		String aplicativo = SettingsRun.getTestData().getParameter("Aplicativo");
		String perfilUsuario = SettingsRun.getTestData().getParameter("Perfil Usuario Administradores");

		if (tipoNovedad != "") {
			this.selectListItemExacto(listTipoNovedad, tipoNovedad);
			DXCUtil.wait(7);
		}
		if (nombreUsuario != "") {
			this.write(inputNombreUsuario, nombreUsuario);
		}
		if (tipoIdentificacionUsu != "") {
			this.selectListItemExacto(listTipoIdentAdmin, tipoIdentificacionUsu);
		}
		if (numeroIdentificacionUsu != "") {
			this.write(inputNumIdent, numeroIdentificacionUsu);
		}
		if (fechaNovedadDesde != "") {
			this.write(inputFechaNovedadDesde, fechaNovedadDesde);
		}
		if (fechaNovedadHasta != "") {
			this.write(inputFechaNovedadHasta, fechaNovedadHasta);
		}
		if (aplicativo != "") {
			this.selectListItemExacto(listAplicativo, aplicativo);
			DXCUtil.wait(5);
		}
		if (perfilUsuario != "") {
			this.selectListItem(listPerfilUsuario, perfilUsuario);
		}
		Evidence.save("Párametros de búsqueda");

		this.click(btnBuscar);
		cont = 0;

		do {
			cont++;
			DXCUtil.wait(1);
		} while (this.element(tablaResultadosActAdmin) == null && this.element(msjAlerta) == null && cont < 7);

		if (this.element(msjAlerta) != null) {
			mensajes = "Se visualiza mensaje " + "[" + this.getText(msjAlerta) + "]";
			Evidence.save("Mensaje de alerta");
			return resultadoPrueba;
		}
		if (this.element(tablaResultadosActAdmin) == null) {
			mensajes = "No se encontraron resultados con los parámetros de búsqueda dados.";
			Evidence.saveFullPage("No se encontro resultados de búsqueda", this);
			return resultadoPrueba;
		}
		Evidence.saveFullPage("Resultados búsqueda", this);
		Reporter.reportEvent(Reporter.MIC_INFO, "Se encontraron resultados con los parámetros de búsqueda dados.");

		String cambiarIDVerDetalle = "";
		WebElement verDetalle = null;
		cambiarIDVerDetalle = verDetalleID.replace("FECHA-HORA", FechaHoraNovedad);
		// Se da clic en el texto ver detalle que coincida con la fecha-hora ingresa en
		// la columna FechaHoraNovedad en el excel
		do {
			verDetalle = this.element((By.xpath(cambiarIDVerDetalle)));
			if (this.element(verDetalle) == null) {
				if (this.element(btnSiguiente) == null) {
					break;
				}
				this.click(btnSiguiente);
				DXCUtil.wait(3);
				Evidence.saveFullPage("tabla resultados", this);

			} else {
				this.click(verDetalle);
				cont = 0;
				do {
					cont++;
					DXCUtil.wait(1);
				} while (this.element(btnRegresar) == null && cont < 6);
				Evidence.saveFullPage("Informe consultas novedades de administración", this);
				this.click(btnRegresar);
				cont = 0;
				do {
					cont++;
					DXCUtil.wait(1);
				} while (this.element(tablaResultadosActAdmin) == null && cont < 6);
				mensajes = "Se encontró resultado de informe actividades administradores con fecha-Hora " + "["
						+ FechaHoraNovedad + "] de manera exitosa.";
				resultadoPrueba = true;
				break;
			}
		} while (this.element(verDetalle) == null);

		if (resultadoPrueba == false) {
			mensajes = "No se encuentró ningún registro que coincida con la fecha-Hora " + "[" + FechaHoraNovedad + "]";
		}

		// se descarga archivo con resultados obtenidos
		if (guardarArchivo.contentEquals("Imprimir")) {
			imprimirArchivos(btnImprimirLista, btnImprimirImpresora, btnSalir, "Actividades Administradores");

		} else if (guardarArchivo.contentEquals("Guardar")) {
			if (this.element(btnGuardar) != null) {
				this.click(btnGuardar);
				DXCUtil.wait(6);
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Se descarga el archivo de informe Actividades Administradores correctamente.");
			} else {
				Reporter.reportEvent(Reporter.MIC_WARNING,
						"No permitio descargar archivo de informe Actividades Administradores, no se encontró el botón guardar.");
			}
		} else if (guardarArchivo.contentEquals("Imprimir y Guardar")) {
			imprimirArchivos(btnImprimirLista, btnImprimirImpresora, btnSalir, "Actividades Administradores");

			if (this.element(btnGuardar) != null) {
				this.click(btnGuardar);
				DXCUtil.wait(6);
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Se descarga el archivo de informe Actividades Administradores correctamente.");
			} else {
				Reporter.reportEvent(Reporter.MIC_WARNING,
						"No permitio descargar archivo de informe Actividades Administradores, no se encontró el botón guardar.");
			}
		}

		return resultadoPrueba;
	}

	// =======================================================================================================================

	public boolean cobrosEfectivos() throws Exception {
		boolean resultadoPrueba = false;

		if (tipoIdentificacionUsu != "") {
			this.selectListItemExacto(listTipoIdentifEmp, tipoIdentificacionUsu);
		}
		if (numeroIdentificacionUsu != "") {
			this.write(inputNumIdentifEmp, numeroIdentificacionUsu);
		}
		if (fechaNovedadDesde != "") {
			this.write(inputFechaNovedadDesdeCobros, fechaNovedadDesde);
		}
		if (fechaNovedadHasta != "") {
			this.write(inputFechaNovedadHastaCobros, fechaNovedadHasta);
		}
		if (servicios != "") {
			this.selectListItemExacto(listServicios, servicios);
		}
		if (nombreEmpresa != "") {
			this.write(inputNombreEmpCobros, nombreEmpresa);
		}
		Evidence.save("Párametros de búsqueda");
		this.click(btnBuscar);
		cont = 0;

		do {
			cont++;
			DXCUtil.wait(1);
		} while (this.element(tablaResultadosCobros) == null && this.element(msjAlerta) == null && cont < 7);

		if (this.element(msjAlerta) != null) {
			mensajes = "Se visualiza mensaje " + "[" + this.getText(msjAlerta) + "]";
			Evidence.save("Mensaje de alerta");
			return resultadoPrueba;
		}
		if (this.element(tablaResultadosCobros) == null) {
			mensajes = "No se encontraron resultados con los parámetros de búsqueda dados.";
			Evidence.saveFullPage("No se encontro resultados de búsqueda", this);
			return resultadoPrueba;
		}
		Evidence.saveFullPage("Resultados búsqueda", this);
		Reporter.reportEvent(Reporter.MIC_INFO, "Se encontraron resultados con los parámetros de búsqueda dados.");

		String cambiarIDVerDetalle = "";
		WebElement verDetalle = null;
		cambiarIDVerDetalle = verDetalleCobros.replace("FECHA-HORA", FechaHoraNovedad);
		// Se da clic en el texto ver detalle que coincida con la fecha-hora ingresa en
		// la columna FechaHoraNovedad en el excel
		do {
			verDetalle = this.element((By.xpath(cambiarIDVerDetalle)));
			if (this.element(verDetalle) == null) {
				if (this.element(btnSiguiente) == null) {
					break;
				}
				this.click(btnSiguiente);
				DXCUtil.wait(3);
				Evidence.saveFullPage("tabla resultados", this);

			} else {
				this.click(verDetalle);
				cont = 0;
				do {
					cont++;
					DXCUtil.wait(1);
				} while (this.element(btnGuardarCobros) == null && cont < 6);
				Evidence.saveFullPage("Informe cobros efectivos", this);
				Reporter.reportEvent(Reporter.MIC_INFO,
						"En opción ver detalle se procede a  guardar informe de cobros efectivos");
				this.click(btnGuardarCobros);
				DXCUtil.wait(5);
				this.click(btnVolver);
				cont = 0;
				do {
					cont++;
					DXCUtil.wait(1);
				} while (this.element(tablaResultadosCobros) == null && cont < 6);
				mensajes = "Se encontró resultado de informe cobros efectivos con fecha-Hora " + "[" + FechaHoraNovedad
						+ "] de manera exitosa.";
				resultadoPrueba = true;
				break;
			}
		} while (this.element(verDetalle) == null);

		if (resultadoPrueba == false) {
			mensajes = "No se encuentró ningún registro que coincida con la fecha-Hora " + "[" + FechaHoraNovedad + "]";
		}

		// se descarga archivo con resultados obtenidos
		if (guardarArchivo.contentEquals("Imprimir")) {
			imprimirArchivos(btnImprimirListaOpcion2, btnImprimirImpresora, btnSalir, "Cobros Efectivos");

		} else if (guardarArchivo.contentEquals("Guardar")) {
			if (this.element(btnGuardar) != null) {
				this.click(btnGuardar);
				DXCUtil.wait(6);
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Se descarga el archivo de informe Cobros Efectivos con los resultados de búsqueda encontrados correctamente.");
			} else {
				Reporter.reportEvent(Reporter.MIC_WARNING,
						"No permitio descargar archivo de informe Cobros Efectivos, no se encontró el botón guardar.");
			}
		} else if (guardarArchivo.contentEquals("Imprimir y Guardar")) {
			imprimirArchivos(btnImprimirListaOpcion2, btnImprimirImpresora, btnSalir, "Cobros Efectivos");

			if (this.element(btnGuardar) != null) {
				this.click(btnGuardar);
				DXCUtil.wait(6);
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Se descarga el archivo de informe Cobros Efectivos con los resultados de búsqueda encontrados correctamente.");
			} else {
				Reporter.reportEvent(Reporter.MIC_WARNING,
						"No permitio descargar archivo de informe Cobros Efectivos, no se encontró el botón guardar.");
			}
		}

		return resultadoPrueba;
	}

	public boolean informeEmpresas() throws Exception {

		String comboServicios = SettingsRun.getTestData().getParameter("Combo de servicios");
		String ventaDigital = SettingsRun.getTestData().getParameter("Venta Digital");
		

		boolean resultadosPrueba = false;

		if (fechaNovedadDesde != "") {
			this.write(inputFechaNovedadDesde, fechaNovedadDesde);
		}
		if (fechaNovedadHasta != "") {
			this.write(inputFechaNovedadHasta, fechaNovedadHasta);
		}
		if (comboServicios != "") {
			this.selectListItemExacto(listCombosdeServicio, comboServicios);
		}
		if (ventaDigital != "") {
			this.selectListItemExacto(listVentaDigital, ventaDigital);
		}

		if (estado != "") {
			this.selectListItemExacto(listEstado, estado);
		}
		Evidence.saveFullPage("Parámetros de búsqueda", this);
		this.click(btnBuscar);

		cont = 0;

		do {
			cont++;
			DXCUtil.wait(1);
		} while (this.element(tablaResultadosEmpresas) == null && this.element(msjAlerta) == null && cont < 7);

		if (this.element(msjAlerta) != null) {
			mensajes = "Se visualiza mensaje " + "[" + this.getText(msjAlerta) + "]";
			Evidence.save("Mensaje de alerta");
			return resultadosPrueba;
		}
		if (this.element(tablaResultadosEmpresas) == null) {
			mensajes = "No se encontraron resultados con los parámetros de búsqueda dados.";
			Evidence.saveFullPage("No se encontro resultados de búsqueda", this);
			return resultadosPrueba;
		}

		mensajes = "Se encontraron resultados con los parámetros de búsqueda dados de manera exitosa";
		resultadosPrueba = true;

		boolean verificarClickSiguiente = false;
		do {
			Evidence.saveFullPage("Tabla Resultados", this);
			this.focus(columnaTablaResultTeléfonoAdmin);
			Evidence.saveFullPage("Tabla Resultados parte 2", this);
			this.focus(columnaTablaResultFechaCancelacion);
			Evidence.saveFullPage("Tabla Resultados parte 3", this);
			if (this.element(btnSiguiente) != null) {
				this.click(btnSiguiente);
				verificarClickSiguiente = true;

			}
			DXCUtil.wait(3);
		} while (this.element(btnSiguiente) != null);
// se agrega if para tomar pantallas de la tabla cuando se tenga el botón siguiente desahabilitado

		if (verificarClickSiguiente == true && this.element(btnSiguiente) == null) {
			Evidence.saveFullPage("Tabla Resultados", this);
			this.focus(columnaTablaResultTeléfonoAdmin);
			Evidence.saveFullPage("Tabla Resultados parte 2", this);
			this.focus(columnaTablaResultFechaCancelacion);
			Evidence.saveFullPage("Tabla Resultados parte 3", this);
		}
// se descarga archivo con resultados obtenidos

		if (guardarArchivo.contentEquals("Imprimir")) {
			imprimirArchivos(btnImprimirLista, btnImprimirImpresora, btnSalir, "Informe de Empresas");

		} else if (guardarArchivo.contentEquals("Guardar")) {
			if (this.element(btnGuardar) != null) {
				this.click(btnGuardar);
				DXCUtil.wait(6);
				Reporter.reportEvent(Reporter.MIC_INFO, "Se descarga el archivo de informe de empresas correctamente.");
			} else {
				Reporter.reportEvent(Reporter.MIC_WARNING,
						"No permitio descargar archivo de informe de empresas, no se encontró el botón guardar.");
			}
		} else if (guardarArchivo.contentEquals("Imprimir y Guardar")) {
			imprimirArchivos(btnImprimirLista, btnImprimirImpresora, btnSalir, "Informe de Empresas");

			if (this.element(btnGuardar) != null) {
				this.click(btnGuardar);
				DXCUtil.wait(6);
				Reporter.reportEvent(Reporter.MIC_INFO, "Se descarga el archivo de informe de empresas correctamente.");
			} else {
				Reporter.reportEvent(Reporter.MIC_WARNING,
						"No permitio descargar archivo de informe de empresas, no se encontró el botón guardar.");
			}
		}

		return resultadosPrueba;
	}

	public boolean estádisticasTransacciones() throws Exception {

		boolean resultadoPrueba = false;

		if (fechaNovedadDesde != "") {
			this.element(btnCalendarInicial).click();
			do {
				DXCUtil.wait(1);
			} while (this.element(By.xpath("//*[@id='cphCuerpo_txtFecha_CalendarExtender1_container']")) == null);
			this.selectDatePicker2("//*[@id='cphCuerpo_txtFecha_CalendarExtender1_container']", fechaNovedadDesde);

		}

		if (fechaNovedadHasta != "") {
			this.element(btnCalendarHasta).click();
			do {
				DXCUtil.wait(1);
			} while (this.element(By.xpath("//*[@id='cphCuerpo_CalendarExtender1_container']")) == null);
			this.selectDatePicker2("//*[@id='cphCuerpo_CalendarExtender1_container']", fechaNovedadHasta);

		}
		if (tipoIdentifEmp != "") {
			this.selectListItemExacto(tipoIdentificacionEmp, tipoIdentifEmp);
		}
		if (numeroIDEmpresa != "") {
			this.write(numIdentEmp, numeroIDEmpresa);
		}

		if (clienteEmpresarial != "") {
			this.write(inputClienteEmpresarial, clienteEmpresarial);
		}
		this.click(btnBuscar);

		cont = 0;

		do {
			cont++;
			DXCUtil.wait(1);
		} while (this.element(tablaResultadosEstadisticasPortal) == null && this.element(msjAlerta) == null
				&& cont < 7);

		if (this.element(msjAlerta) != null) {
			mensajes = "Se visualiza mensaje " + "[" + this.getText(msjAlerta)
					+ "] en informe Estadísticas de transacciones con los parametros de busqueda dados.";
			Evidence.save("Mensaje de alerta");
			return resultadoPrueba;
		}
		if (this.element(tablaResultadosEstadisticasPortal) == null) {
			mensajes = "No se encontraron resultados con los parámetros de búsqueda dados.";
			Evidence.saveFullPage("No se encontro resultados de búsqueda", this);
			return resultadoPrueba;
		}

		mensajes = "Se encontraron resultados con los parámetros de búsqueda dados de manera exitosa";
		resultadoPrueba = true;

		boolean verificarClickSiguiente = false;
		do {
			Evidence.saveFullPage("Tabla Resultados", this);
			this.focus(ProvAchValor);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 2", this);
			this.focus(LibafcValor);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 3", this);
			this.focus(trccDestinosCorresponsalValor);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 4", this);
			this.focus(trccQrValor);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 5", this);
			if (this.element(btnSiguiente) != null) {
				DXCUtil.wait(1);
				DXCUtil.waitMilisegundos(500);
				this.click(btnSiguiente);
				verificarClickSiguiente = true;

			}
			DXCUtil.wait(3);
		} while (this.element(btnSiguiente) != null);
// se agrega if para tomar pantallas de la tabla cuando se tenga el botón siguiente desahabilitado

		if (verificarClickSiguiente == true && this.element(btnSiguiente) == null) {
			Evidence.saveFullPage("Tabla Resultados", this);
			this.focus(ProvAchValor);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 2", this);
			this.focus(LibafcValor);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 3", this);
			this.focus(trccDestinosCorresponsalValor);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 4", this);
			this.focus(trccQrValor);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 5", this);
		}
// se descarga archivo con resultados obtenidos

		if (guardarArchivo.contentEquals("Imprimir")) {
			imprimirArchivos(btnImprimirListaEstadisticas, btnImprimirImpresora, btnSalir,
					"Informe de estadísticas de transacciones");

		} else if (guardarArchivo.contentEquals("Guardar")) {
			if (this.element(btnGuardar) != null) {
				this.click(btnGuardar);
				DXCUtil.wait(6);
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Se descarga el archivo de informe estadísticas de transacciones correctamente.");
			} else {
				Reporter.reportEvent(Reporter.MIC_WARNING,
						"No permitio descargar archivo de informe estadísticas de transacciones, no se encontró el botón guardar.");
			}
		} else if (guardarArchivo.contentEquals("Imprimir y Guardar")) {
			imprimirArchivos(btnImprimirListaEstadisticas, btnImprimirImpresora, btnSalir,
					"estadísticas de transacciones");

			if (this.element(btnGuardar) != null) {
				this.click(btnGuardar);
				DXCUtil.wait(6);
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Se descarga el archivo de informe estadísticas de transacciones correctamente.");
			} else {
				Reporter.reportEvent(Reporter.MIC_WARNING,
						"No permitio descargar archivo de informe estadísticas de transacciones, no se encontró el botón guardar.");
			}
		}

		return resultadoPrueba;
	}

	// =======================================================================================================================
	public boolean informeOperativo() throws Exception {
		boolean resultadoPrueba = false;

		if (servicios != "") {
			this.selectListItemExacto(listServicios, servicios);
		}
		if (fechaNovedadDesde != "") {
			DXCUtil.wait(1);
			this.element(btnCalendarInicial).click();
			do {
				DXCUtil.wait(1);
			} while (this.element(By.xpath("//*[@id='cphCuerpo_txtFecha_CalendarExtender1_container']")) == null);
			this.selectDatePicker2("//*[@id='cphCuerpo_txtFecha_CalendarExtender1_container']", fechaNovedadDesde);
		}

		if (fechaNovedadHasta != "") {
			this.element(btnCalendarHasta).click();
			do {
				DXCUtil.wait(1);
			} while (this.element(By.xpath("//*[@id='cphCuerpo_CalendarExtender1_container']")) == null);
			this.selectDatePicker2("//*[@id='cphCuerpo_CalendarExtender1_container']", fechaNovedadHasta);
		}
		this.click(btnBuscar);
		cont = 0;

		do {
			cont++;
			DXCUtil.wait(1);
		} while (this.element(tablaResultadosOperativo) == null && this.element(msjAlerta) == null && cont < 8);

		if (this.element(msjAlerta) != null) {
			mensajes = "Se visualiza mensaje " + "[" + this.getText(msjAlerta)
					+ "] en informe operativo con los parámetros de búsqueda dados.";
			Evidence.save("Mensaje de alerta");
			return resultadoPrueba;
		}
		if (this.element(tablaResultadosOperativo) == null) {
			mensajes = "No se encontraron resultados con los parámetros de búsqueda dados.";
			Evidence.saveFullPage("No se encontro resultados de búsqueda", this);
			return resultadoPrueba;
		}

		Reporter.reportEvent(Reporter.MIC_INFO, "Se encontraron resultados con los parámetros de búsqueda dados");
		

		
		
		
		int numImpresionPDF = 1;
		
		do {

			
			Evidence.saveFullPage("Resultados de búsqueda", this);

			if (this.element(tablaResultadosOperativo) != null && this.element(tablaResultadosOperativo).getText().contains(FechaHoraNovedad)) {
				
				List<WebElement> filas = this.findElements(filasTablaResultadosOperativo);
				List<WebElement> btnVerDetalle = new ArrayList<>();

				for (WebElement fila : filas) {
					if (fila.getText().contains(FechaHoraNovedad)) {

						btnVerDetalle.add(fila.findElement(By.xpath("//td[contains(text(),'" + FechaHoraNovedad + "')]/following-sibling::td")));
					}

				} 

				for (int i = 0; i < btnVerDetalle.size(); i++) {

					if (tablaResultadosOperativo != null) {
//						System.out.println("("+filasTablaResultadosString+"//td[contains(text(),'" + FechaHoraNovedad + "')]/following-sibling::td)[" + i + "]");
						WebElement verDetalle = this.element("("+filasTablaResultadosString+"//td[contains(text(),'" + FechaHoraNovedad + "')]/following-sibling::td)[" + (i+1) + "]");
						verDetalle.click();
						cont = 0;
						do {
							cont++;
							DXCUtil.wait(1);
						} while (this.element(btnRegresar) == null && cont < 6);
						
						// se descarga archivo con resultados obtenidos
						if (guardarArchivo.contentEquals("Imprimir")) {
							imprimirArchivos(btnImprimirLista, btnImprimirImpresora, btnSalir,
									"Informe operativo '" + numImpresionPDF +"'");
							numImpresionPDF++;
							cont=0;
							do {
								cont++;
								DXCUtil.wait(1);
							} while (this.element(tablaResultadosOperativo) == null && this.element(msjAlerta) == null && cont < 8);

						} else if (guardarArchivo.contentEquals("Guardar")) {
							if (this.element(btnGuardar) != null) {
								this.click(btnGuardar);
								DXCUtil.wait(6);
								Reporter.reportEvent(Reporter.MIC_INFO,
										"Se descarga el archivo de informe operativo de transacciones correctamente.");
							} else {
								Reporter.reportEvent(Reporter.MIC_WARNING,
										"No permitio descargar archivo de informe operativo, no se encontró el botón guardar.");
							}
						} else if (guardarArchivo.contentEquals("Imprimir y Guardar")) {
							imprimirArchivos(btnImprimirLista, btnImprimirImpresora, btnSalir,
									"Informe operativo '" + numImpresionPDF +"'");
							numImpresionPDF++;

							if (this.element(btnGuardar) != null) {
								this.click(btnGuardar);
								DXCUtil.wait(6);
								Reporter.reportEvent(Reporter.MIC_INFO,
										"Se descarga el archivo de informe operativo correctamente.");
							} else {
								Reporter.reportEvent(Reporter.MIC_WARNING,
										"No permitio descargar archivo de informe operativo, no se encontró el botón guardar.");
							}
						}
						
						
						Evidence.saveFullPage("ver detalle - Informe operativo", this);
						this.click(btnRegresar);
					}
					cont = 0;
					do {
						cont++;
						DXCUtil.wait(1);
					} while (this.element(tablaResultadosOperativo) == null && cont < 7);

				}
				resultadoPrueba = true;
				break;

			} else {
				DXCUtil.wait(2);
				if (this.element(btnSiguiente) != null && this.element(btnSiguiente).isEnabled()) {
					this.click(btnSiguiente);
					DXCUtil.wait(2);

					
				} else {
					
 
					if (!this.element(tablaResultadosOperativo).getText().contains(FechaHoraNovedad)) {
						break;
					}
				}

			}

		} while (true);



		if (resultadoPrueba == false) {
			mensajes = "No se encuentró ningún registro que coincida con la fecha " + "[" + FechaHoraNovedad + "]";
		}else {
			mensajes = "Se encontró resultado de informe operativo con fecha " + "["
			+ FechaHoraNovedad + "] de manera exitosa.";
		}
		
		// se descarga archivo con resultados obtenidos

		if (guardarArchivo.contentEquals("Imprimir")) {
			imprimirArchivos(btnImprimirListaOpcion2, btnImprimirImpresora, btnSalir,
					"Informe operativo");
			cont=0;
			do {
				cont++;
				DXCUtil.wait(1);
			} while (this.element(tablaResultadosOperativo) == null && this.element(msjAlerta) == null && cont < 8);

		} else if (guardarArchivo.contentEquals("Guardar")) {
			if (this.element(btnGuardar) != null) {
				this.click(btnGuardar);
				DXCUtil.wait(6);
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Se descarga el archivo de informe operativo de transacciones correctamente.");
			} else {
				Reporter.reportEvent(Reporter.MIC_WARNING,
						"No permitio descargar archivo de informe operativo, no se encontró el botón guardar.");
			}
		} else if (guardarArchivo.contentEquals("Imprimir y Guardar")) {
			imprimirArchivos(btnImprimirListaOpcion2, btnImprimirImpresora, btnSalir,
					"Informe operativo");

			if (this.element(btnGuardar) != null) {
				this.click(btnGuardar);
				DXCUtil.wait(6);
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Se descarga el archivo de informe operativo correctamente.");
			} else {
				Reporter.reportEvent(Reporter.MIC_WARNING,
						"No permitio descargar archivo de informe operativo, no se encontró el botón guardar.");
			}
		}
		
		
		 

		resultadoPrueba = true;

		return resultadoPrueba;
	}
	
	// =======================================================================================================================
	public boolean pagosMasivos() throws Exception {
		boolean resultadoPrueba = false;
		String fecha = SettingsRun.getTestData().getParameter("Fecha Pagos");
		String perfilUsuarioPagosMasivos = SettingsRun.getTestData().getParameter("Perfil Usuario Pagos");
		String numeroProceso = SettingsRun.getTestData().getParameter("Nro Proceso");
		String nombreProceso = SettingsRun.getTestData().getParameter("Nombre  Proceso");
		String productoOrigen = SettingsRun.getTestData().getParameter("Producto origen");
		
		
		if (fecha!= "") {
			this.selectListItemExacto(listFecha, fecha);
		}
		if (fechaNovedadDesde!= "") {
			this.write(inputFechaNovedadDesde, fechaNovedadDesde);
		}
		if (fechaNovedadHasta!= "") {
			this.write(inputFechaNovedadHasta, fechaNovedadHasta);
		}
		
		if (estado !="") {
			this.selectListItemExacto(listEstados, estado);
		}
		
		if (numeroProceso !="") {
			this.write(inputNroProceso, numeroProceso);
		}
		if (perfilUsuarioPagosMasivos != "") {
			this.selectListItem(listPerfilUsuario, perfilUsuarioPagosMasivos);
		}
		
		if (nombreProceso!="") {
			this.write(inputNombreProceso, nombreProceso);
		}
		
		if (productoOrigen!= "") {
			this.selectListItem(listProductoOrigen, productoOrigen);
		}
		
		if (tipoIdentifEmp != "") {
			this.selectListItemExacto(listTipoIdentifEmp, tipoIdentifEmp);
		}
		if (numeroIDEmpresa != "") {
			this.write(inputNumIdentifEmp, numeroIDEmpresa);
		}
		
		if (nombreEmpresa != "") {
			this.write(inputNombreEmpCobros, nombreEmpresa);
		}
		
		if (tipoAutenticación != "") {
			this.selectListItem(listTipoAutenticacion, tipoAutenticación);
		}
		
		
		
		this.click(btnBuscar);
		
		cont = 0;

		do {
			cont++;
			DXCUtil.wait(1);
		} while (this.element(tablaResultadoPagosMasiv) == null && this.element(msjAlerta) == null && cont < 8);

		if (this.element(msjAlerta) != null) {
			mensajes = "Se visualiza mensaje " + "[" + this.getText(msjAlerta)
					+ "] en informe Pagos Masivos con los parámetros de búsqueda dados.";
			Evidence.save("Mensaje de alerta");
			return resultadoPrueba;
		}
		if (this.element(tablaResultadoPagosMasiv) == null) {
			mensajes = "No se encontraron resultados con los parámetros de búsqueda dados.";
			Evidence.saveFullPage("No se encontro resultados de búsqueda", this);
			return resultadoPrueba;
		}
		
		
		Evidence.saveFullPage("Resultados búsqueda", this);
		Reporter.reportEvent(Reporter.MIC_INFO, "Se encontraron resultados con los parámetros de búsqueda dados.");
		
		if (numeroProceso != "") {
				
		String cambiarIdNroProceso = "";
		WebElement numProceso = null;
		cambiarIdNroProceso = armarXpathNumeroProceso.replace("NUMERO_PROCESO", numeroProceso);

		// Se da clic en el texto  que coincida con el numero de proceso y se  descarga la información del proceso
		
		do {
			numProceso = this.element((By.xpath(cambiarIdNroProceso)));
			if (this.element(numProceso) == null) {
				if (this.element(btnSiguiente) == null) {
					break;
				}
				this.click(btnSiguiente);
				DXCUtil.wait(3);
				Evidence.saveFullPage("tabla resultados", this);

			} else {
				this.click(numProceso);
				cont = 0;
				do {
					cont++;
					DXCUtil.wait(1);
				} while (this.element(btnRegresar) == null && cont < 6);
				Evidence.saveFullPage("Información número proceso", this);
				// se descarga archivo con resultados obtenidos
				if (guardarArchivo.contentEquals("Imprimir")) {
					imprimirArchivos(btnImprimirLista, btnImprimirImpresora, btnSalir, "informe pagos masivos 1");

				} else if (guardarArchivo.contentEquals("Guardar")) {
					if (this.element(btnGuardar) != null) {
						this.click(btnGuardar);
						DXCUtil.wait(6);
						Reporter.reportEvent(Reporter.MIC_INFO,
								"Se descarga el archivo de informe pagos masivos correctamente.");
					} else {
						Reporter.reportEvent(Reporter.MIC_WARNING,
								"No permitio descargar archivo de informe pagos masivos, no se encontró el botón guardar.");
					}
				} else if (guardarArchivo.contentEquals("Imprimir y Guardar")) {
					imprimirArchivos(btnImprimirLista, btnImprimirImpresora, btnSalir, "informe pagos masivos 1");

					if (this.element(btnGuardar) != null) {
						this.click(btnGuardar);
						DXCUtil.wait(6);
						Reporter.reportEvent(Reporter.MIC_INFO,
								"Se descarga el archivo de informe pagos masivos correctamente.");
					} else {
						Reporter.reportEvent(Reporter.MIC_WARNING,
								"No permitio descargar archivo de informe pagos masivos, no se encontró el botón guardar.");
					}
				}
				this.click(btnRegresar);
				cont = 0;
				do {
					cont++;
					DXCUtil.wait(1);
				} while (this.element(tablaResultadoPagosMasiv) == null && cont < 6);
				mensajes = "Se encontró resultado de informe pagos masivos con numero de proceso " + "["
						+ numeroProceso + "] de manera exitosa.";
				resultadoPrueba = true;
				break;
			}
		} while (this.element(numProceso) == null);
		
		if (resultadoPrueba == false) {
			mensajes = "No se encuentró ningún registro que coincida con el numero de proceso " + "[" + numeroProceso + "]";
		}
		
		}else if (numeroProceso == "") {
			Reporter.reportEvent(Reporter.MIC_INFO, "No se realiza búsqueda de número de proceso en informe dado que el campo no se diligencio en archivo de lanzamiento.");
		}
		
		
		
		// se descarga archivo con resultados obtenidos
		if (guardarArchivo.contentEquals("Imprimir")) {
			imprimirArchivos(btnImprimirListaOpcion3, btnImprimirImpresora, btnSalir, "informe pagos masivos");

		} else if (guardarArchivo.contentEquals("Guardar")) {
			if (this.element(btnGuardar) != null) {
				this.click(btnGuardar);
				DXCUtil.wait(6);
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Se descarga el archivo de informe pagos masivos correctamente.");
			} else {
				Reporter.reportEvent(Reporter.MIC_WARNING,
						"No permitio descargar archivo de informe pagos masivos, no se encontró el botón guardar.");
			}
		} else if (guardarArchivo.contentEquals("Imprimir y Guardar")) {
			imprimirArchivos(btnImprimirListaOpcion3, btnImprimirImpresora, btnSalir, "informe pagos masivos");

			if (this.element(btnGuardar) != null) {
				this.click(btnGuardar);
				DXCUtil.wait(6);
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Se descarga el archivo de informe pagos masivos correctamente.");
			} else {
				Reporter.reportEvent(Reporter.MIC_WARNING,
						"No permitio descargar archivo de informe pagos masivos, no se encontró el botón guardar.");
			}
		}


		
		mensajes = "Se finaliza validación de informe pagos masivos exitosamente";
		
		
		
		
		
		return resultadoPrueba;
	
	}
	
	
	// =======================================================================================================================
	public boolean pagosYTransferencias() throws Exception {
	boolean resultadoPrueba = false;
	
	String fecha = SettingsRun.getTestData().getParameter("Fecha Pagos");
	String perfilUsuarioPagos = SettingsRun.getTestData().getParameter("Perfil Usuario Pagos");
	String numeroProceso = SettingsRun.getTestData().getParameter("Nro Proceso");
	String nombreProceso = SettingsRun.getTestData().getParameter("Nombre  Proceso");
	String productoOrigen = SettingsRun.getTestData().getParameter("Producto origen");
	String productoDestino = SettingsRun.getTestData().getParameter("Producto Destino");
	String pagosQR = SettingsRun.getTestData().getParameter("Pagos QR");
	
	if (fecha!= "") {
		this.selectListItemExacto(listFecha, fecha);
	}
	if (fechaNovedadDesde!= "") {
		this.write(inputFechaNovedadDesde, fechaNovedadDesde);
	}
	if (fechaNovedadHasta!= "") {
		this.write(inputFechaNovedadHasta, fechaNovedadHasta);
	}
	
	if (estado !="") {
		this.selectListItemExacto(listEstados, estado);
	}
	
	if (numeroProceso !="") {
		this.write(inputNroProceso, numeroProceso);
	}
	if (perfilUsuarioPagos != "") {
		this.selectListItem(listPerfilUsuario, perfilUsuarioPagos);
	} 
	
	if (nombreProceso!="") {
		this.write(inputNombreProceso, nombreProceso);
	}
	
	if (productoOrigen != "") {
		this.write(inputNumCuentaOrigen, productoOrigen);
	}
	
	if (productoDestino != null) {
		this.write(inputNumCuentaDestino, productoDestino);
	}
	
	 
	if (tipoIdentifEmp != "") {
		this.selectListItemExacto(listTipoIdentifEmp, tipoIdentifEmp);
	}
	if (numeroIDEmpresa != "") {
		this.write(inputNumIdentifEmp, numeroIDEmpresa);
	}
	
	if (nombreEmpresa != "") {
		this.write(inputNombreEmpCobros, nombreEmpresa);
	}
	
	if (servicios != "") {
		this.selectListItemExacto(listServiciosPagos, servicios);
	}
	
	if (tipoAutenticación != "") {
		this.selectListItem(listTipoAutenticacion, tipoAutenticación);
	}
	
	if (pagosQR != "") {
		this.selectListItem(listPagosQR, pagosQR);
	}
	
	this.click(btnBuscar);
	
	cont = 0;

	do {
		cont++;
		DXCUtil.wait(1);
	} while (this.element(tablaResultadosPagosYTransf) == null && this.element(msjAlerta) == null && cont < 8);

	if (this.element(msjAlerta) != null) {
		mensajes = "Se visualiza mensaje " + "[" + this.getText(msjAlerta)
				+ "] en informe Pagos Masivos con los parámetros de búsqueda dados.";
		Evidence.save("Mensaje de alerta");
		return resultadoPrueba;
	}
	if (this.element(tablaResultadosPagosYTransf) == null) {
		mensajes = "No se encontraron resultados con los parámetros de búsqueda dados.";
		Evidence.saveFullPage("No se encontro resultados de búsqueda", this);
		return resultadoPrueba;
	}
	
	
	Evidence.saveFullPage("Resultados búsqueda", this);
	Reporter.reportEvent(Reporter.MIC_INFO, "Se encontraron resultados con los parámetros de búsqueda dados.");

	if (FechaHoraNovedad !="") {
		
	
	String xpathBuscarPago = "";
	WebElement buscaPago = null;
	String [] partes = FechaHoraNovedad.split(" ");
	String campofecha = partes[0];
	String campoHora = partes[1];
	
	if (campoHora == "") {
		String [] partes2 = FechaHoraNovedad.split("  ");
		campoHora = partes2[2];
	}
	xpathBuscarPago = buscarPagoTransf.replace("FECHA", campofecha).replace("HORA", campoHora);
	
	
	// Se da clic en el texto ver detalle que coincida con la fecha-hora ingresa en
	// la columna FechaHoraNovedad en el excel
	do {
		buscaPago = this.element((By.xpath(xpathBuscarPago)));
		if (this.element(buscaPago) == null) {
			if (this.element(btnSiguiente) == null) {
				resultadoPrueba = false;
				mensajes = "No se encontró resultado de informe Pagos y Transferencias con fecha-Hora " + "["
						+ FechaHoraNovedad + "] con los parámetros de búsqueda dados.";
				break;
			}
			this.click(btnSiguiente);
			DXCUtil.wait(3);
			Evidence.saveFullPage("tabla resultados", this);

		} else {
			Evidence.saveFullPage("Pago -transferencia con fecha-hora encontrado",this);
			this.focus(tablaResultadosColumnaServicio);
			Evidence.saveFullPage("Pago -transferencia con fecha-hora encontrado 2",this);			
			mensajes = "Se encontró resultado de informe Pagos y Transferencias con fecha-Hora " + "["
					+ FechaHoraNovedad + "] de manera exitosa.";
			resultadoPrueba = true;
			break;
		}
	} while (this.element(buscaPago) == null);
	
	} else {
		Reporter.reportEvent(Reporter.MIC_INFO, "No se realiza búsqueda de pago o transferencia por fecha y hora en informe dado que el campo no se diligencio en archivo de lanzamiento.");
	}
	// se descarga archivo con resultados obtenidos
			if (guardarArchivo.contentEquals("Imprimir")) {
				imprimirArchivos(btnImprimirLista, btnImprimirImpresora, btnSalir, "informe Pagos y Transferencias");

			} else if (guardarArchivo.contentEquals("Guardar")) {
				if (this.element(btnGuardar) != null) {
					this.click(btnGuardar);
					DXCUtil.wait(6);
					Reporter.reportEvent(Reporter.MIC_INFO,
							"Se descarga el archivo de informe pagos y transferencias correctamente.");
				} else {
					Reporter.reportEvent(Reporter.MIC_WARNING,
							"No permitio descargar archivo de informe pagos y transferencias, no se encontró el botón guardar.");
				}
			} else if (guardarArchivo.contentEquals("Imprimir y Guardar")) {
				imprimirArchivos(btnImprimirLista, btnImprimirImpresora, btnSalir, "informe pagos y transferencias");

				if (this.element(btnGuardar) != null) {
					this.click(btnGuardar);
					DXCUtil.wait(6);
					Reporter.reportEvent(Reporter.MIC_INFO,
							"Se descarga el archivo de informe pagos y transferencias correctamente.");
				} else {
					Reporter.reportEvent(Reporter.MIC_WARNING,
							"No permitio descargar archivo de informe pagos y transferencias, no se encontró el botón guardar.");
					
				}
			}	
	
	if (FechaHoraNovedad =="") {
		mensajes = "Se finaliza validación de informe pagos y transferencias exitosamente";
	}
	
	
	
	
	return resultadoPrueba;
	}
	
	// =======================================================================================================================
	public boolean serviciosContratados() throws Exception {
		
		boolean resultadoPrueba = false;
		
		String fechaModificacionDesde = SettingsRun.getTestData().getParameter("Fecha Modificación Desde");
		String fechaModificacionHasta = SettingsRun.getTestData().getParameter("Fecha Modificación Hasta");
		
		Reporter.reportEvent(Reporter.MIC_INFO, "Se ingresa al informe Servicios Contratados, se procede a realizar filtro de búsqueda");
		
		if (clienteEmpresarial != "") {
			this.write(inputClienteEmpresarial, clienteEmpresarial);
		}
		
		if (tipoIdentificacionUsu != "") {
			this.selectListItemExacto(listTipoIdentAdmin, tipoIdentificacionUsu);
		}
		
		if (numeroIdentificacionUsu != "") {
			this.write(inputNumIdentif, numeroIdentificacionUsu);
		}
		
		if (nombreEmpresa != "") {
			this.write(inputNombreEmpCobros, nombreEmpresa);
		}
		
		if (servicios != "") {
			this.selectListItemExacto(listServicios, servicios);
		}
		
		if (fechaNovedadDesde != "") {
			this.element(btnFechaCreacionDesde).click();
			do {
				DXCUtil.wait(1);
			} while (this.element(By.xpath("//*[@id='cphCuerpo_CalendarExtender1_popupDiv']")) == null); 
			this.selectDatePicker2("//*[@id='cphCuerpo_CalendarExtender1_popupDiv']", fechaNovedadDesde);
			DXCUtil.wait(1);
		}
		
		if (fechaNovedadHasta != "") {
			this.element(btnFechaCreacionHasta).click();
			do {
				DXCUtil.wait(1);
			} while (this.element(By.xpath("//*[@id='cphCuerpo_CalendarExtender2_popupDiv']")) == null); 
			this.selectDatePicker2("//*[@id='cphCuerpo_CalendarExtender2_popupDiv']", fechaNovedadHasta);
			DXCUtil.wait(1);
		}
		
		if (fechaModificacionDesde != "") {
			this.element(btnFechaModificacionDesde).click();
			do {
				DXCUtil.wait(1);
			} while (this.element(By.xpath("//*[@id='cphCuerpo_CalendarExtender3_popupDiv']")) == null); 
			this.selectDatePicker2("//*[@id='cphCuerpo_CalendarExtender3_popupDiv']", fechaModificacionDesde);
			DXCUtil.wait(1);
		}
		
		if (fechaModificacionHasta != "") {
			this.element(btnFechaModificacionHasta).click();
			do {
				DXCUtil.wait(1);
			} while (this.element(By.xpath("//*[@id='cphCuerpo_CalendarExtender4_popupDiv']")) == null); 
			this.selectDatePicker2("//*[@id='cphCuerpo_CalendarExtender4_popupDiv']", fechaModificacionHasta);
			
		}
		
		this.click(btnBuscar);
		
		
		
		
		cont = 0;

		do {
			cont++;
			DXCUtil.wait(1);
		} while (this.element(tablaResultadosServicios) == null && this.element(msjAlerta) == null && cont < 8);

		if (this.element(msjAlerta) != null) {
			mensajes = "Se visualiza mensaje " + "[" + this.getText(msjAlerta)
					+ "] en informe servicios contratados con los parámetros de búsqueda dados.";
			Evidence.save("Mensaje de alerta");
			return resultadoPrueba;
		}
		if (this.element(tablaResultadosServicios) == null) {
			mensajes = "No se encontraron resultados con los parámetros de búsqueda dados.";
			Evidence.saveFullPage("No se encontro resultados de búsqueda", this);
			return resultadoPrueba;
		}
		
		
		
		boolean verificarClickSiguiente = false;
		
		if (this.element(btnSiguiente) != null) {
			
		
		do {
			
			Evidence.saveFullPage("Tabla Resultados", this);
			this.focus(tablaResultDescSobreRangoDestinoTarjPrep);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 2", this);
			this.focus(tablaResultNroTransaccRangoSuperior);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 3", this);
			this.focus(tablaResultTarifaEspecial);
			Evidence.saveFullPage("Tabla Resultados parte 4", this);
						
			if (this.element(btnSiguiente) != null) {
				this.click(btnSiguiente);
				verificarClickSiguiente = true;

			}
			DXCUtil.wait(3);
		} while (this.element(btnSiguiente) != null);
		// se agrega if para tomar pantallas de la tabla cuando se tenga el botón siguiente desahabilitado
		if (verificarClickSiguiente == true && this.element(btnSiguiente) == null) {
			Evidence.saveFullPage("Tabla Resultados", this);
			this.focus(tablaResultDescSobreRangoDestinoTarjPrep);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 2", this);
			this.focus(tablaResultNroTransaccRangoSuperior);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 3", this);
			this.focus(tablaResultTarifaEspecial);
			Evidence.saveFullPage("Tabla Resultados parte 4", this);
		}
		
		}else {
			Evidence.saveFullPage("Tabla Resultados", this);
			this.focus(tablaResultDescSobreRangoDestinoTarjPrep);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 2", this);
			this.focus(tablaResultNroTransaccRangoSuperior);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 3", this);
			this.focus(tablaResultTarifaEspecial);
			Evidence.saveFullPage("Tabla Resultados parte 4", this);
		}
		
		
		mensajes = "Se encontraron resultados con los parámetros de búsqueda dados de manera exitosa";
		resultadoPrueba = true;
		
		
		// se descarga archivo con resultados obtenidos
					if (guardarArchivo.contentEquals("Imprimir")) {
						imprimirArchivos(btnImprimirLista, btnImprimirImpresora, btnSalir, "Informe Servicios Contratados");

					} else if (guardarArchivo.contentEquals("Guardar")) {
						if (this.element(btnGuardar) != null) {
							this.click(btnGuardar);
							DXCUtil.wait(6);
							Reporter.reportEvent(Reporter.MIC_INFO,
									"Se descarga el archivo de informe servicios contratados correctamente.");
						} else {
							Reporter.reportEvent(Reporter.MIC_WARNING,
									"No permitio descargar archivo de informe servicios contratados, no se encontró el botón guardar.");
						}
					} else if (guardarArchivo.contentEquals("Imprimir y Guardar")) {
						imprimirArchivos(btnImprimirLista, btnImprimirImpresora, btnSalir, "Informe Servicios Contratados");

						if (this.element(btnGuardar) != null) {
							this.click(btnGuardar);
							DXCUtil.wait(6);
							Reporter.reportEvent(Reporter.MIC_INFO,
									"Se descarga el archivo de informe servicios contratados correctamente.");
						} else {
							Reporter.reportEvent(Reporter.MIC_WARNING,
									"No permitio descargar archivo de informe servicios contratados, no se encontró el botón guardar.");
							
						}
					}	
		
		
		
		
		return resultadoPrueba;
	
	}
	// =======================================================================================================================
	public boolean transaccionesACH() throws Exception {
	boolean resultadoPrueba =false;
	
	if (fechaNovedadDesde != "") {
		this.element(btnfechaInicialConsulta).click();
		do {
			DXCUtil.wait(1);
			
		} while (this.element(By.xpath("//*[@id='cphCuerpo_txtFecha_CalendarExtender1_popupDiv']")) == null); 
		this.selectDatePicker2("//*[@id='cphCuerpo_txtFecha_CalendarExtender1_popupDiv']", fechaNovedadDesde);
		DXCUtil.wait(1);
	}
	if (fechaNovedadHasta != "") {
		this.element(btnfechaFinalConsulta).click();
		do {
			DXCUtil.wait(1);
			
		} while (this.element(By.xpath("//*[@id='cphCuerpo_CalendarExtender1_popupDiv']")) == null); 
		this.selectDatePicker2("//*[@id='cphCuerpo_CalendarExtender1_popupDiv']", fechaNovedadHasta);
		DXCUtil.wait(1); 
	}
	 
	this.click(btnBuscar);
	
	cont = 0;

	do {
		cont++;
		DXCUtil.wait(1);
	} while (this.element(tablaResultadosTransACH) == null && this.element(msjAlerta) == null && cont < 8);

	if (this.element(msjAlerta) != null) {
		mensajes = "Se visualiza mensaje " + "[" + this.getText(msjAlerta)
				+ "] en informe de transacciones sin respuesta ACH con los parámetros de búsqueda dados.";
		Evidence.save("Mensaje de alerta");
		return resultadoPrueba;
	}
	if (this.element(tablaResultadosTransACH) == null) {
		mensajes = "No se encontraron resultados con los parámetros de búsqueda dados.";
		Evidence.saveFullPage("No se encontro resultados de búsqueda", this);
		return resultadoPrueba;
	}
	
	
	
	if (this.element(btnSiguiente) != null) {
			
		boolean verificarClickSiguiente = false;
		do {
			
			Evidence.saveFullPage("Tabla Resultados", this);
			this.focus(tablaResultadosColumnaBanco);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 2", this);
									
			if (this.element(btnSiguiente) != null) {
				this.click(btnSiguiente);
				verificarClickSiguiente = true;

			}
			DXCUtil.wait(3);
		} while (this.element(btnSiguiente) != null);
		// se agrega if para tomar pantallas de la tabla cuando se tenga el botón siguiente desahabilitado
		if (verificarClickSiguiente == true && this.element(btnSiguiente) == null) {
			Evidence.saveFullPage("Tabla Resultados", this);
			this.focus(tablaResultadosColumnaBanco);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 2", this);
		}
		
		}else {
			Evidence.saveFullPage("Tabla Resultados", this);
			this.focus(tablaResultadosColumnaBanco);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 2", this);
		}
	
	
	// se descarga archivo con resultados obtenidos
	if (guardarArchivo.contentEquals("Imprimir")) {
		imprimirArchivos(btnImprimirLista, btnImprimirImpresora, btnSalir, "Informe transacciones sin respuesta Ach");

	} else if (guardarArchivo.contentEquals("Guardar")) {
		if (this.element(btnGuardar) != null) {
			this.click(btnGuardar);
			DXCUtil.wait(6);
			Reporter.reportEvent(Reporter.MIC_INFO,
					"Se descarga el archivo de informe transacciones sin respuesta Ach correctamente.");
		} else {
			Reporter.reportEvent(Reporter.MIC_WARNING,
					"No permitio descargar archivo de informe transacciones sin respuesta Ach, no se encontró el botón guardar.");
		}
	} else if (guardarArchivo.contentEquals("Imprimir y Guardar")) {
		imprimirArchivos(btnImprimirLista, btnImprimirImpresora, btnSalir, "Informe transacciones sin respuesta Ach");

		if (this.element(btnGuardar) != null) {
			this.click(btnGuardar);
			DXCUtil.wait(6);
			Reporter.reportEvent(Reporter.MIC_INFO,
					"Se descarga el archivo de informe transacciones sin respuesta Ach correctamente.");
		} else {
			Reporter.reportEvent(Reporter.MIC_WARNING,
					"No permitio descargar archivo de informe transacciones sin respuesta Ach, no se encontró el botón guardar.");
			
		}
	}
	mensajes = "Se encontraron resultados con los parámetros de búsqueda dados de manera exitosa.";
	resultadoPrueba = true;
	
	
	return resultadoPrueba;
	
	}
	// =======================================================================================================================
	public boolean transaccionesRealizadas() throws Exception {
		boolean resultadoPrueba = false;
		
		
		if (fechaNovedadDesde != "") {
			this.element(btnCalendarInicial).click();
			do {
				DXCUtil.wait(1);
			} while (this.element(By.xpath("//*[@id='cphCuerpo_txtFecha_CalendarExtender1_container']")) == null);
			this.selectDatePicker2("//*[@id='cphCuerpo_txtFecha_CalendarExtender1_container']", fechaNovedadDesde);

		}

		if (fechaNovedadHasta != "") {
			this.element(btnCalendarHasta).click();
			do {
				DXCUtil.wait(1);
			} while (this.element(By.xpath("//*[@id='cphCuerpo_CalendarExtender1_container']")) == null);
			this.selectDatePicker2("//*[@id='cphCuerpo_CalendarExtender1_container']", fechaNovedadHasta);
		}
	this.click(btnBuscar);
	
	cont = 0;

	do {
		cont++;
		DXCUtil.wait(1);
	} while (this.element(tablaResultUsuariosCreados) == null && this.element(msjAlerta) == null && cont < 8);

	if (this.element(msjAlerta) != null) {
		mensajes = "Se visualiza mensaje " + "[" + this.getText(msjAlerta)
				+ "] en informe Usuarios Creados con los parámetros de búsqueda dados.";
		Evidence.save("Mensaje de alerta");
		return resultadoPrueba;
	}
	if (this.element(tablaResultUsuariosCreados) == null) {
		mensajes = "No se encontraron resultados con los parámetros de búsqueda dados.";
		Evidence.saveFullPage("No se encontro resultados de búsqueda", this);
		return resultadoPrueba;
	}
	
	
	if (this.element(btnSiguiente) != null) {
		
		boolean verificarClickSiguiente = false;
		do {
			
			Evidence.saveFullPage("Tabla Resultados", this);
											
			if (this.element(btnSiguiente) != null) {
				this.click(btnSiguiente);
				verificarClickSiguiente = true;

			}
			DXCUtil.wait(3);
		} while (this.element(btnSiguiente) != null);
		// se agrega if para tomar pantallas de la tabla cuando se tenga el botón siguiente desahabilitado
		if (verificarClickSiguiente == true && this.element(btnSiguiente) == null) {
			Evidence.saveFullPage("Tabla Resultados", this);
		}
		
		}else {
			Evidence.saveFullPage("Tabla Resultados", this);			
		}
	
	
	// se descarga archivo con resultados obtenidos
	if (guardarArchivo.contentEquals("Imprimir")) {
		imprimirArchivos(btnImprimirListaOpcion2, btnImprimirImpresora, btnSalir, "Informe transacciones realizadas");

	} else if (guardarArchivo.contentEquals("Guardar")) {
		if (this.element(btnGuardar) != null) {
			this.click(btnGuardar);
			DXCUtil.wait(6);
			Reporter.reportEvent(Reporter.MIC_INFO,
					"Se descarga el archivo de informe transacciones realizadas correctamente.");
		} else {
			Reporter.reportEvent(Reporter.MIC_WARNING,
					"No permitio descargar archivo de informe realizadas, no se encontró el botón guardar.");
		}
	} else if (guardarArchivo.contentEquals("Imprimir y Guardar")) {
		imprimirArchivos(btnImprimirListaOpcion2, btnImprimirImpresora, btnSalir, "Informe realizadas");

		if (this.element(btnGuardar) != null) {
			this.click(btnGuardar);
			DXCUtil.wait(6);
			Reporter.reportEvent(Reporter.MIC_INFO,
					"Se descarga el archivo de informe transacciones realizadas correctamente.");
		} else {
			Reporter.reportEvent(Reporter.MIC_WARNING,
					"No permitio descargar archivo de informe transacciones realizadas, no se encontró el botón guardar.");
			
		}
	}	
	mensajes = "Se encontraron resultados con los parámetros de búsqueda dados de manera exitosa.";
	resultadoPrueba = true;
	
	
			
	
			return resultadoPrueba;
	}
	// =======================================================================================================================
	public boolean usuariosCreados() throws Exception {
	boolean resultadoPrueba = false;
	
	if (clienteEmpresarial != "") {
		this.write(inputClienteEmpresarial, clienteEmpresarial);
	}
	
	if (fechaNovedadDesde != "") {
		this.element(btnCalendarInicial).click();
		do {
			DXCUtil.wait(1);
		} while (this.element(By.xpath("//*[@id='cphCuerpo_txtFecha_CalendarExtenderDesde_popupDiv']")) == null);
		this.selectDatePicker2("//*[@id='cphCuerpo_txtFecha_CalendarExtenderDesde_popupDiv']", fechaNovedadDesde);
		DXCUtil.wait(1);
	}
	
	if (fechaNovedadHasta != "") {
		this.element(btnFechaCreacionHasta).click();
		do {
			DXCUtil.wait(1);
		} while (this.element(By.xpath("//*[@id='cphCuerpo_txtFecha_CalendarExtenderHasta_popupDiv']")) == null); 
		this.selectDatePicker2("//*[@id='cphCuerpo_txtFecha_CalendarExtenderHasta_popupDiv']", fechaNovedadHasta);
		
	}
	
	if (tipoIdentifEmp != "") {
		this.selectListItemExacto(listTipoIdentifEmp, tipoIdentifEmp);
	}
	
	if (numeroIDEmpresa != "") {
		this.write(inputNumIdentifEmp, numeroIDEmpresa);
	}
	
	if (tipoAutenticación != "") {
		this.selectListItem(listTipoAutenticacion, tipoAutenticación);
	}
	this.click(btnBuscar);
	
	
	
	
	cont = 0;

	do {
		cont++;
		DXCUtil.wait(1);
	} while (this.element(tablaResultUsuariosCreados) == null && this.element(msjAlerta) == null && cont < 8);

	if (this.element(msjAlerta) != null) {
		mensajes = "Se visualiza mensaje " + "[" + this.getText(msjAlerta)
				+ "] en Informe usuarios creados con los parámetros de búsqueda dados.";
		Evidence.save("Mensaje de alerta");
		return resultadoPrueba;
	}
	if (this.element(tablaResultUsuariosCreados) == null) {
		mensajes = "No se encontraron resultados con los parámetros de búsqueda dados.";
		Evidence.saveFullPage("No se encontro resultados de búsqueda", this);
		return resultadoPrueba;
	}
	
	
	if (this.element(btnSiguiente) != null) {
		
		boolean verificarClickSiguiente = false;
		do {
			
			Evidence.saveFullPage("Tabla Resultados", this);
			this.focus(nombreColumnaTablaNombreEmp);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 2", this);
											
			if (this.element(btnSiguiente) != null) {
				this.click(btnSiguiente);
				verificarClickSiguiente = true;

			}
			DXCUtil.wait(3);
		} while (this.element(btnSiguiente) != null);
		// se agrega if para tomar pantallas de la tabla cuando se tenga el botón siguiente desahabilitado
		if (verificarClickSiguiente == true && this.element(btnSiguiente) == null) {
			Evidence.saveFullPage("Tabla Resultados", this);
			this.focus(nombreColumnaTablaNombreEmp);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 2", this);
		}
		
		}else {
			Evidence.saveFullPage("Tabla Resultados", this);
			this.focus(nombreColumnaTablaNombreEmp);
			DXCUtil.waitMilisegundos(500);
			Evidence.saveFullPage("Tabla Resultados parte 2", this);			
		}
	
	
	// se descarga archivo con resultados obtenidos
	if (guardarArchivo.contentEquals("Imprimir")) {
		imprimirArchivos(btnImprimirListaOpcion3, btnImprimirImpresora, btnSalir, "Informe usuarios creados");

	} else if (guardarArchivo.contentEquals("Guardar")) {
		if (this.element(btnGuardar) != null) {
			this.click(btnGuardar);
			DXCUtil.wait(6);
			Reporter.reportEvent(Reporter.MIC_INFO,
					"Se descarga el archivo de informe usuarios creados correctamente.");
		} else {
			Reporter.reportEvent(Reporter.MIC_WARNING,
					"No permitio descargar archivo de informe usuarios creados, no se encontró el botón guardar.");
		}
	} else if (guardarArchivo.contentEquals("Imprimir y Guardar")) {
		imprimirArchivos(btnImprimirListaOpcion3, btnImprimirImpresora, btnSalir, "Informe usuarios creados");

		if (this.element(btnGuardar) != null) {
			this.click(btnGuardar);
			DXCUtil.wait(6);
			Reporter.reportEvent(Reporter.MIC_INFO,
					"Se descarga el archivo de informe usuarios creados correctamente.");
		} else {
			Reporter.reportEvent(Reporter.MIC_WARNING,
					"No permitio descargar archivo de informe usuarios creados, no se encontró el botón guardar.");
			
		}
	}	
	mensajes = "Se encontraron resultados con los parámetros de búsqueda dados de manera exitosa.";
	resultadoPrueba = true;
	
	
	
	
	return resultadoPrueba;
	
	}
	// =======================================================================================================================
	public boolean aprobacionesApp() throws Exception {
	boolean resultadoPrueba = false;

	String fecha = SettingsRun.getTestData().getParameter("Fecha Pagos");
	String perfilUsuarioPagos = SettingsRun.getTestData().getParameter("Perfil Usuario Pagos");
	String numeroProceso = SettingsRun.getTestData().getParameter("Nro Proceso");
	String nombreProceso = SettingsRun.getTestData().getParameter("Nombre  Proceso");
	String productoOrigen = SettingsRun.getTestData().getParameter("Producto origen");
	String productoDestino = SettingsRun.getTestData().getParameter("Producto Destino");
	String pagosQR = SettingsRun.getTestData().getParameter("Pagos QR");
	 

	if (fechaNovedadDesde!= "") {
		this.write(inputFechaNovedadDesde, fechaNovedadDesde);
	}
	if (fechaNovedadHasta!= "") {
		this.write(inputFechaNovedadHasta, fechaNovedadHasta);
	}
	
	if (estado !="") {
		this.selectListItemExacto(listEstadoAprobacion, estado);
	} 
	
	if (numeroProceso !="") {
		this.write(inputNroProceso, numeroProceso);
	}
	if (perfilUsuarioPagos != "") {
		this.selectListItem(perfilUsuarioAprob, perfilUsuarioPagos);
	} 
	
	if (nombreProceso!="") {
		this.write(inputNombreProceso, nombreProceso);
	}
	
	if (productoOrigen != "") {
		this.selectListItem(productoOrigenAprob, productoOrigen);
	}
	
	
	if (tipoIdentifEmp != "") {
		this.selectListItemExacto(listTipoIdentificacionEmp, tipoIdentifEmp);
	}
	if (numeroIDEmpresa != "") {
		this.write(inputNumIdentifEmp, numeroIDEmpresa);
	}
	
	if (tipoAutenticación != "") {
		this.selectListItem(listTipoAutenticacion, tipoAutenticación);
	}
	
	if (clienteEmpresarial != "") {
		this.write(inputClienteEmpre, clienteEmpresarial);
	}
		
	if (pagosQR != "") {
		this.selectListItem(listPagosQR, pagosQR);
	}
	
	this.click(btnBuscar);
	
	return resultadoPrueba;
	}
	
	// =======================================================================================================================
	
	public boolean informesAutogestion() throws Exception {
		boolean resultadoPrueba = false;
		String novedadAutogestion = SettingsRun.getTestData().getParameter("Novedad Autogestión");
		String canal = SettingsRun.getTestData().getParameter("Canal");
		
		if (novedadAutogestion != "") {
			this.selectListItem(listNovedadAutoGest, novedadAutogestion);
		}
		
		if (fechaNovedadDesde != "") {
			this.write(inputFechaNovedadDesde, fechaNovedadDesde);
		}
		if (fechaNovedadHasta != "") {
			this.write(inputFechaNovedadHasta, fechaNovedadHasta);
		}
	
		
		if (clienteEmpresarial != "") {
			this.write(inputClienteEmpre, clienteEmpresarial);
		}
		
		if (tipoIdentifEmp != "") {
			this.selectListItemExacto(listTipoIdentificacionEmp, tipoIdentifEmp);
		}
		if (numeroIDEmpresa != "") {
			this.write(inputNumIdentifEmp, numeroIDEmpresa);
		}
		
		if (tipoIdentificacionUsu != "") {
			this.selectListItemExacto(listTipoIdentifUsu, tipoIdentificacionUsu);
		}
		
		if (numeroIdentificacionUsu != "") {
			this.write(inputNumIdentiUsuario, numeroIdentificacionUsu);
		}
		if (canal != "") {
			this.selectListItem(listCanalAutoges, canal);
		}
		this.click(btnBuscar);
		
		
		return resultadoPrueba;
		}
	
	
	// =======================================================================================================================
	public boolean informeRetirosSinTarjeta() throws Exception {
		boolean resultadoPrueba = false;
		String numeroProceso = SettingsRun.getTestData().getParameter("Nro Proceso");
		String productoOrigen = SettingsRun.getTestData().getParameter("Producto origen");
		String canalRetiro = SettingsRun.getTestData().getParameter("Canal Retiro");
		String canal = SettingsRun.getTestData().getParameter("Canal");
		
		if (fechaNovedadDesde != "") {
			this.write(inputFechaNovedadDesde, fechaNovedadDesde);
		}
		if (fechaNovedadHasta != "") {
			this.write(inputFechaNovedadHasta, fechaNovedadHasta);
		}
		
		if (estado !=  "") {
			this.selectListItem(listEstados,estado);
		}
		
		if (numeroProceso != "") {
			this.write(inputNroProceso, numeroProceso);
		}
		
		if (productoOrigen != "") {
			DXCUtil.waitMilisegundos(500);
			this.write(inputCuentaOrig, productoOrigen);
		}
		
		if (numeroIDEmpresa != "") {
			this.write(inputNumIdentifEmp, numeroIDEmpresa);
		}
		
		if (nombreEmpresa != "") {
			this.write(inputNombreEmpCobros, nombreEmpresa);
		}
		if (tipoAutenticación != "") {
			this.selectListItem(metodoAprobacion,tipoAutenticación);
		}
		
		if (canalRetiro != "") {
			this.selectListItem(listCanalRetiro, canalRetiro);
		}
		
		if (canal != "") {
			this.selectListItem(listCanalR, canal);
		}
		this.click(btnBuscar);
		
		
		
		
		
		
		
		
		
		return resultadoPrueba;
	}
	// =======================================================================================================================
	public boolean negociosFiduciarios() throws Exception {
		
		boolean resultadoPrueba = false;
		
		String novedadFiducia = SettingsRun.getTestData().getParameter("Novedad - Negocios Fiduciarios");
		String moduloFiducia = SettingsRun.getTestData().getParameter("Modulo -Negocios Fiduciarios");
		
		
		do {
			DXCUtil.wait(1);
		} while (this.element(iFrameNegociosFiduciarios) == null);
		this.changeFrame(this.element(iFrameNegociosFiduciarios));
		this.click(btnInformeActividades);
		do {
			DXCUtil.wait(1);
		} while (this.element(listNovedad)==null);
		
		if (novedadFiducia != "") {
			this.selectListItem(listNovedad, novedadFiducia);
		}
		
		if (nombreUsuario != "") {
			this.write(inputNombreUsu, nombreUsuario);
		}
		
		if (fechaNovedadDesde != "") {
			this.element(inputFechaNovDesde).click();
			do {
				DXCUtil.wait(1);
			} while (this.element(By.xpath("//*[@class ='datepicker-days']")) == null); 
			this.selectDatePickerFiducia(fechaNovedadDesde);
		}
		if (fechaNovedadHasta != "") {
			this.click(inputFechaNovHasta);
			
		}
		
		if (tipoIdentificacionUsu != "") {
			this.selectListItemExacto(listTipoIdent, tipoIdentificacionUsu);
		}
		
		if (numeroIdentificacionUsu != "") {
			this.write(inputNumeroIdentifi, numeroIdentificacionUsu);
		}
		
		if (moduloFiducia != "") {
			this.selectListItem(listModulo, moduloFiducia);
		}
		this.click(btnBuscarNegocios);
		
		
		this.changeToDefaultFrame();
		return resultadoPrueba;
	}
	
	// =======================================================================================================================
	
	public boolean informeTransaccionesFic() throws Exception {
	boolean resultadoPrueba = false;
	
	String tipodeTX = SettingsRun.getTestData().getParameter("Tipo TX");
	
	if (clienteEmpresarial != "") {
		this.write(inputClienteEmpresarial, clienteEmpresarial);
	}
	
	if (fechaNovedadDesde != "") {
		this.element(fechaConsultaDesde).click();
		do {
			DXCUtil.wait(1);
		} while (this.element(By.xpath("//*[@id='cphCuerpo_txtFecha_CalendarExtenderDesde_popupDiv']")) == null);
		this.selectDatePicker2("//*[@id='cphCuerpo_txtFecha_CalendarExtenderDesde_popupDiv']", fechaNovedadDesde);
		
	}
	
	if (fechaNovedadHasta != "") {
		this.element(fechaConsultaHasta).click();
		do {
			DXCUtil.wait(1);
		} while (this.element(By.xpath("//*[@id='cphCuerpo_txtFecha_CalendarExtenderHasta_popupDiv']")) == null);
		this.selectDatePicker2("//*[@id='cphCuerpo_txtFecha_CalendarExtenderHasta_popupDiv']", fechaNovedadHasta);
	}
	if (tipoIdentifEmp != "") {
		this.selectListItemExacto(listTipoIdentifEmp, tipoIdentifEmp);
	}
	
	if (numeroIDEmpresa != "") {
		this.write(inputNumIdentifEmp, numeroIDEmpresa);
	}
	
	if (estado != "") {
		this.selectListItem(listEstados, estado);
	}
	
	if (tipodeTX != "") {
		 this.selectListItem(listTipoTX, tipodeTX);
	}
	
	this.click(btnBuscar);
	
	
	
	
	
	
	
	return resultadoPrueba;
	
		
	}
	public boolean informeEstadosEmpresa() throws Exception {
	boolean resultadoPrueba= false;
	
	if (clienteEmpresarial != "") {
	this.write(clientEmp, clienteEmpresarial);
	}
	
	if (numeroIDEmpresa != null) {
		this.write(numberIdEmp, numeroIDEmpresa);
	}
	this.click(btnBuscar);	
	
	return resultadoPrueba;
		
	}
	
	
	public boolean informeTransferenciasInternacionales() throws Exception {
	boolean resultadoPrueba= false;
	
	do {
		DXCUtil.wait(1);
	} while (this.element(iFrameTransfInternacional) == null);
	this.changeFrame(iFrameTransfInternacional);

	
	
	
	
	return resultadoPrueba;
	
	}
	
	
	
	// =======================================================================================================================

	public void imprimirArchivos(By btnImprimirTablaResultados, By btnImprimirSimboloImpresora,
			By btnSalirVentanaImpresion, String nombredelArchivo) throws Exception {

		WebElement imprimirTabla = this.findElement(btnImprimirTablaResultados);
		imprimirTabla.click();
		DXCUtil.wait(3);
		Reporter.reportEvent(Reporter.MIC_INFO, "Se ingresa a la ventana para imprimir la tabla.");

		this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());
		this.pageLogin.maximizeBrowser();

		DXCUtil.wait(3);
//		Evidence.save("Evidencias imprimir archivo", this);
		Evidence.save("Evidencias imprimir archivo");
		this.findElement(btnImprimirSimboloImpresora).click();
		Evidence.saveFullPage("Evidencias imprimir archivo", this);
		DXCUtil.wait(2);

		DXCUtil.BonotesTecla("ENTER");

		// Nombre del archivo que va a aguardar
		String nombreArchivo = Evidence.getNbEvidenceDirectory() + "\\" + nombredelArchivo;
		PageAutogestionServicioMiddle.toPrint(nombreArchivo);
		
		DXCUtil.wait(3);
		if (this.element(btnSalirVentanaImpresion) != null) {
			this.click(btnSalirVentanaImpresion);
		}
		DXCUtil.wait(3);
		this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());
		Reporter.reportEvent(Reporter.MIC_INFO, "Se imprimió correctamente.");

	}
	// =======================================================================================================================

	public static String getMensajes() {
		return mensajes;
	}
	// =======================================================================================================================

}
