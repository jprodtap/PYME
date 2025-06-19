package dav.Consultas_Y_Extractos;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.lang.Math;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.poi.hssf.record.DConRefRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import dav.pymes.PagePortalPymes;
import dav.pymes.moduloCrearTx.ControllerCrearTx;
import dav.pymes.moduloCrearTx.PageOrigen;
import dav.transversal.DatosDavivienda;
import dav.transversal.MovimientoStratus;
import dav.transversal.Stratus;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageConsultasyExtractos extends PagePortalPymes {

	ControllerCrearTx controller = null;
	PageOrigen pageOrigen = null;

	public final static String MSG_EXITO_GUARD = "Esta transacción"; // INDICA QUE FALTA FIRMA
	public final static String MSG_EXITO_PAGO = "Su transacción ha sido recibida por el Portal"; // 1FIRMA / APROBACIÓN
																									// DESDE DETALLE
	public final static String MSG_EXITO_APROB = "Usted aprobó"; // APROBACIÓN DESDE CAJA
	public final static String MSG_EXITO_SER = "Su transacción ha sido recibida por el Portal PYME. Programe el pago automatico de este servicio, dando click aquí";

	By locPasoPageTxEnviada = By
			.xpath("//div[@id='UpMasterTitulo']/span[text()='Transacciones Realizadas en el Portal PYME']");
	By locPasoPageTxReali = By
			.xpath("//div[@id='UpMasterTitulo']/span[text()='Transacciones Realizadas en el Portal PYME']");
	By locLinkTxAprob = By.xpath("//span[@id='lblMasterAlerta']/a[@cssclass='LetraHypervinculoAzul']");
	By locPasoPage = By.xpath("//div[@id='UpMasterTitulo']/span[text()='Transacciones Realizadas en el Portal PYME']");
	String docuDetalle = "//table[@id='cphCuerpo_gvTransacciones']//a[contains(text(),'NUM_TX')]";
	By lblMasterTitulo = By.xpath("//*[@id='lblMasterTitulo']");
	By table = By.xpath("//*[@id='cphCuerpo_gvFondosDaviplus']/tbody | //table[@id='cphCuerpo_gvMovimientosCuentas']//tbody | //*[@id='cphCuerpo_gvMovimientosTarjetaDeCredito']/tbody | //*[@id='cphCuerpo_gvMovimientosCredito']");

	By tbDestinoFina = By.xpath("//*[@id='cphCuerpo_gvTransaccionesMasivas']/tbody");
	By tipoDestinoFondosMismoNit = By.id("cphCuerpo_lblERTipoProductoDestino");
	// Este locator sirve como referencia para cuenta inscrita y no inscrita
	By tipoDestinoFondosCta = By.xpath("//*[@id='cphCuerpo_lblERCUTipoProductoDestino' or @id='cphCuerpo_lblTipoProductoDestinoPAGP']");
	By tipoDestinoServicios = By.id("cphCuerpo_lblTipoPase");
//	-----------------------------------------------------------------------------------------------------------------------

	By TitulosaldosConsolidados = By.xpath("//*[@id='lblMasterTitulo']");
	String saldosConsolidadosFinanciacion = "//tr[td[contains(text(), 'TIPO_PROD')] and td[contains(text(), 'NUM_PROD')]]/td/select";

	By MenuModu = By.id("mnMenu");
	By ListProduc = By.id("cphCuerpo_ddlproducto");
	By ListProduc2 = By.id("cphCuerpo_ddlBusAvanzadaProducto");
	By btnBusqAvanzada = By.id("cphCuerpo_lnkBusquedaAvanzada");
	By btnMostDatos = By.id("cphCuerpo_btnMostrarDatos");
	By btn48 = By.id("cphCuerpo_rdbUltimas48Horas");
	By btn7 = By.id("cphCuerpo_rdbUltimos7dias");
	By btn30 = By.id("cphCuerpo_rdbUltimos30dias");

	By btnDesArch = By.cssSelector("#cphCuerpo_ddlDescargaMasiva, #cphCuerpo_BtDaviplus");
	By btnseleArch = By.cssSelector("#cphCuerpo_ddlDescargaMasiva, #cphCuerpo_DropDafuturo");
	By btnDescargaMasiva = By.cssSelector("#cphCuerpo_btnDescargaMasiva,#cphCuerpo_BtDaviplus");

	By nomArchDes = By.id("cphCuerpo_txtNombreArchivo");
	By cmpFechadesde = By.id("cphCuerpo_txtFechaDesde");
	By cmpFechaInicio = By.id("cphCuerpo_txtFechaHasta");
	By btnSiguiente = By.id("cphCuerpo_lnkSiguiente_cuentas");

	By tabla1ahorroeinversiones = By.xpath("//*[@id='cphCuerpo_gvSaldosConsolidados']//tbody");
	By tabla2financiación = By.xpath("//*[@id='cphCuerpo_gvSaldosConsolidadosFinanciacion']//tbody");
	By tabla3ahorroeinversiones = By.xpath("//*[@id='cphCuerpo_gvahorroinversiones']//tbody");
	By tabla4financiación = By.xpath("//*[@id='cphCuerpo_gvFinanciacion']//tbody");

	By total1 = By.xpath(
			"//*[@id='cphCuerpo_gvSaldosConsolidados']//tbody//tr//td[@align='right']//b[contains(text(),'Total')]//parent::td//parent::tr//td[3]");
	By total2 = By.xpath(
			"//*[@id='cphCuerpo_gvSaldosConsolidadosFinanciacion']//tbody//tr//td[@align='right']//b[contains(text(),'Total')]//parent::td//parent::tr//td[3]");

	By total3 = By.xpath(
			"//*[@id='cphCuerpo_gvahorroinversiones']//tbody//tr//td[@align='right']//b[contains(text(),'Total')]//parent::td//parent::tr//td[4]");
	By total4 = By.xpath(
			"//*[@id='cphCuerpo_gvFinanciacion']//tbody//tr//td[@align='right']//b[contains(text(),'Total')]//parent::td//parent::tr//td[4]");

	By montoTx = By.id("cphCuerpo_lblMasivoMonto");
	By fechaTx = By.xpath(
			"//span[@id='cphCuerpo_lblFechaPase' or @id='cphCuerpo_lblMasivoFecha' or @id='cphCuerpo_lblERFecha' or @id='cphCuerpo_lblFecEjecPAGP']");
	By horaTx = By
			.xpath("//span[@id='cphCuerpo_lblHoraPase' or @id='cphCuerpo_lblMasivoHora'or @id='cphCuerpo_lblERHora'or @id='cphCuerpo_lblHoraEjecPAGP']");

//	String tiempoconsulta;
	String todaydesde = null, fechahasta = null;

	static String saldoTotalInicial = null;
	static String saldoDisInicial = null;
	static String saldoTotalFinal = null;
	static String saldoDisponibleFinal = null;

	static String numAprova = null;

	static String tipoDes = null;
	static String estadoDes = null;
	static String[] registrosmov = null;

	public PageConsultasyExtractos(BasePageWeb parentPage) {
		super(parentPage);
	}

//=======================================================================================================================================

	public void SetNumApr(String numAproTx) {
		numAprova = numAproTx;
	}

	public void setSaldoTotalInicial(String saldo) {
		this.saldoTotalInicial = saldo;
	}

	public void setSaldoDisInicial(String saldo) {
		this.saldoDisInicial = saldo;
	}

	public void setSaldoTotalFinal(String saldo) {
		this.saldoTotalFinal = saldo;
	}

	public void setSaldoDisponibleFinal(String saldo) {
		this.saldoDisponibleFinal = saldo;
	}

//=======================================================================================================================================	
	/**
	 * Realiza la consulta de las tx realizadas si es de 1 firma da clic en el link
	 * Consulta y Extractos si es de mas firmas busca el menu C
	 * 
	 * @param numAPD
	 * @return
	 * @throws Exception
	 */
	public String ConsultayExtractos(String servicio,String navegador,String tipoIDEmpresa,String numIdEmpresa,String tipoDocumento,String numeroDoc,String tipoProduct,String numeroProducto,boolean primeroGuardar) throws Exception {
		String msgResp = null;
		String texto = null;
		DXCUtil.wait(5);
//		String navegador = SettingsRun.getTestData().getParameter("Navegador").trim();
		texto = this.getText(this.element(MenuModu));

		if (texto.contains("Consultas y Extractos")) {
			texto = "Consultas y Extractos";
		} else if (texto.contains("Consultas")) {
			texto = "Consultas";

		}

		this.pageOrigen = new PageOrigen(this);
		String msgError = this.getMsgAlertIfExist("lblMasterAlerta");
		if (msgError == null)
			msgError = this.getMsgAlertIfExist("lblMasterTransaccionExitosaAlerta");
		if (primeroGuardar) {
			if (msgError.contains(MSG_EXITO_PAGO)) {
				this.waitIngresoModulo("Transacción Enviada");
				WebElement objLinkConsulta = this.element(locLinkTxAprob);
				if (objLinkConsulta != null)
					this.click(objLinkConsulta);
			} else if (msgError.contains(MSG_EXITO_GUARD)) {
				this.waitIngresoModulo("Detalle de Transacción");
				if (navegador.contains("CHROME")) {
					this.pageOrigen.irAOpcion("Transacciones Realizadas en el Portal PYME", texto,"Transacciones a Través del Portal", null);
				} else {
					this.pageOrigen.irAOpcionMoZilla("Transacciones Realizadas en el Portal PYME", texto,"Transacciones a Través del Portal", null,null);

				}
			} else if (msgError.contains(MSG_EXITO_APROB)) {
				WebElement objLinkConsulta = this.element(locLinkTxAprob);
				if (objLinkConsulta != null)
					this.click(objLinkConsulta);
			}
		} else if (msgError.contains(MSG_EXITO_PAGO) && !msgError.contains(MSG_EXITO_SER)) {
			this.waitIngresoModulo("Transacción Enviada");
			WebElement objLinkConsulta = this.element(locLinkTxAprob);
			if (objLinkConsulta != null)
				this.click(objLinkConsulta);
		} else if (msgError.contains(MSG_EXITO_GUARD)) {
			this.waitIngresoModulo("Transacción Pendiente");
			
			if (navegador.contains("CHROME")) {
				this.pageOrigen.irAOpcion("Transacciones Realizadas en el Portal PYME", texto,"Transacciones a Través del Portal", null);
			} else {
				this.pageOrigen.irAOpcionMoZilla("Transacciones Realizadas en el Portal PYME", texto,"Transacciones a Través del Portal", null,null);

			}
		} else if (msgError.contains(MSG_EXITO_SER)) {
			this.waitIngresoModulo("Transacción Enviada");
			
			if (navegador.contains("CHROME")) {
				this.pageOrigen.irAOpcion("Transacciones Realizadas en el Portal PYME", texto,"Transacciones a Través del Portal", null);
			} else {
				this.pageOrigen.irAOpcionMoZilla("Transacciones Realizadas en el Portal PYME", texto,"Transacciones a Través del Portal", null,null);

			}
		}
		boolean tituloesperado = this.waitEspetaTitulo("Transacciones Realizadas en el Portal PYME");
		
		if (tituloesperado ==false) {
			if (navegador.contains("CHROME")) {
				this.pageOrigen.irAOpcion("Transacciones Realizadas en el Portal PYME", texto,"Transacciones a Través del Portal", null);
			} else {
				this.pageOrigen.irAOpcionMoZilla("Transacciones Realizadas en el Portal PYME", texto,"Transacciones a Través del Portal", null,null);

			}
		}
		
		this.waitIngresoModulo("Transacciones Realizadas en el Portal PYME");
		Evidence.saveFullPage("Consulta de tx", this);

		msgResp = this.getMsgAlertIfExist("lblMasterAlerta");
		if (msgResp != null) {

			if (msgResp != null && msgResp.equals("NO SE ENCONTRARON DATOS")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msgResp);
				return msgResp;
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontró el saldo inicial en los movimientos.");

				this.pageOrigen.terminarIteracion();
			}

		}

		String numAptx = docuDetalle.replace("NUM_TX", numAprova);

		DXCUtil.wait(1);
		WebElement getexto = this.element(numAptx);
		texto = this.getText(getexto);
		DXCUtil.wait(4);
		if (texto != null && texto.equals(numAprova)) {

			if (getexto != null)
				this.click(getexto);
			int contador = 0;
			do {
				contador++;
				DXCUtil.wait(1);
				if (contador > 30) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se presento el Titulo");
					this.pageOrigen.terminarIteracion();
				}
			} while (this.element(lblMasterTitulo) == null);

			WebElement getextoTitulo = this.element(lblMasterTitulo);
			String titulo = this.getText(getextoTitulo);

			if (!DatosDavivienda.IS_RISKMOTOR) {
				WebElement getextoMontoTx = this.element(montoTx);
				String montoTxMov = this.getText(getextoMontoTx);

				int contador2 = 0;
				do {
					contador++;
					DXCUtil.wait(1);
					if (contador > 30) {
						Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se presento La fecha de la tx");
						this.pageOrigen.terminarIteracion();
					}
				} while (this.element(fechaTx) == null);

					
				// Obtiene la ultima tabla de los movimientos masivos
				this.registrosmov = RegistrosMovimientos();

				for (String remov : this.registrosmov) {
					String[] registoDestinos = remov.split("\\|");
					if (registoDestinos.length > 2) {
						this.tipoDes = registoDestinos[1];
						this.estadoDes = registoDestinos[3];
					} else {
						this.tipoDes = registoDestinos[0];
						this.estadoDes = registoDestinos[1];
					}
				}
			}
			
			DXCUtil.wait(3);
			if (titulo == null) {
				titulo = "Consultas y Extratos";
			}else if (titulo.isEmpty()) {
				titulo = "Consultas y Extratos";	
			}
			Evidence.saveFullPage(titulo, this);
			if (!DatosDavivienda.IS_RISKMOTOR) {
				DXCUtil.wait(5);
			ValidacionMovimientoStratus(servicio, tipoIDEmpresa,numIdEmpresa,tipoDocumento,numeroDoc,tipoProduct,numeroProducto);
			}
			int contador3 = 1;
			do {
				DXCUtil.wait(1);
				contador3++;
				msgResp = this.getMsgAlertIfExist("lblMasterAlerta");
			} while (msgResp == null && contador3 < 10);

		} else

		{

			return "Transacción [" + texto + "] NO se encuentra en el listado";
		}

		return msgResp;
	}

	public void ValidacionMovimientoStratus(String servicio,String tipoIDEmpresa,String numIdEmpresa,String tipoDocumento,String numeroDoc,String tipoProduct,String numeroProducto) throws Exception {

		WebElement getextoFecha = this.element(fechaTx);
		String FechaTx = this.getText(getextoFecha);
		// fechaInicial - Date que contiene la fecha de consulta inicial.
		Date fechaConsulta = DXCUtil.stringToDate(FechaTx, "dd/MM/yyyy");
		Date FechaConsultaStratus = DXCUtil.dateAdd(fechaConsulta, Calendar.MINUTE, -2);// dejar en 1

		Reporter.reportEvent(Reporter.MIC_INFO, "Fecha y hora de la tx: " + FechaConsultaStratus);
		WebElement getextoHora = this.element(horaTx);
		String horaTxMov = this.getText(getextoHora);

		String fechaHora = FechaTx + horaTxMov;

		String fecha = DXCUtil.dateToString("MMDD");

		horaTxMov = DXCUtil.hourToStringFormat(horaTxMov, "HHmm");
		String horaTxMov1 = DXCUtil.horaAdd("HHmm", horaTxMov, -2);
		String[] fechaHora2 = { fecha + " " + horaTxMov1 };

		// Datos del titular como un array de String Stratus

//		String tipoIDEmpresa = SettingsRun.getTestData().getParameter("Tipo ID Empresa");
//		String numIdEmpresa = SettingsRun.getTestData().getParameter("Numero ID Empresa").trim();
//
//		String tipoDocumento = SettingsRun.getTestData().getParameter("Tipo Identificación");
//		String numeroDoc = SettingsRun.getTestData().getParameter("Id usuario").trim();
//		String tipoProduct = SettingsRun.getTestData().getParameter("Tipo producto origen / Franquicia").trim();
//		String numeroProducto = SettingsRun.getTestData().getParameter("Número producto origen").trim();

		String[] datosTitular = new String[2];

		// Obtiene el tipo de cuenta si es migrada o fondo o tarjeta de cradito
		String tipoProd = Stratus.getTipoCuenta(tipoProduct);

		if (tipoProduct.contains("CREDITO") || tipoProduct.contains("CRED") || tipoProduct.contains("ompanita")) {
			// Datos usuario
			datosTitular[0] = tipoIDEmpresa;
			datosTitular[1] = numIdEmpresa;

		} else {
			// Datos usuario
			datosTitular[0] = tipoDocumento;
			datosTitular[1] = numeroDoc;
		}
		// Ventanas en consulta de movimientos Stratus
		String[] arrayVents = { MovimientoStratus.VENT_UNID, MovimientoStratus.VENT_IVA, MovimientoStratus.VENT_SALDO,
				MovimientoStratus.VENT_EMERG };
		// Validacion con Stratus
		if (DatosDavivienda.STRATUS != null) {

			// Realiza la consulta en Stratus de todos los movimientos

			List<MovimientoStratus> datosCtadh2 = DatosDavivienda.STRATUS.getMovimientosEnRangoStratus(tipoProd,
					numeroProducto, arrayVents, FechaConsultaStratus, 1, datosTitular);

			if (datosCtadh2 != null) {
				List<String[]> movimientosStratus = new ArrayList<>();

				String saldoinicialstra = this.saldoTotalInicial;
				String saldoDislstra = this.saldoDisInicial;

				String saldofinal = this.saldoTotalFinal;
				String saldoDisponiblefinal = this.saldoDisponibleFinal;

				// Saldo inicial
				double saldoTotalInicial = 0.0;
				double saldoDisponible = 0.0;
				saldoTotalInicial = Double.parseDouble(saldoinicialstra);
				String saldoPos = String.format("%.1f", saldoTotalInicial);
				saldoDisponible = Double.parseDouble(saldoDislstra);

				// Saldo final
				double saldoTotalFinal = 0.0;
				double saldoDisponibleFinal = 0.0;
				saldoTotalFinal = Double.parseDouble(saldofinal);
				saldoDisponibleFinal = Double.parseDouble(saldoDisponiblefinal);
				String saldoPosFinal = String.format("%.2f", saldoTotalFinal);
				Reporter.write(" ");
				Reporter.write("*** CONSULTA DE MOVIMIENTOS EN STRATUS");
				Reporter.write(" ");
				Reporter.write(
						"           FECHA | HORA |   TALON  | TIPO  |  OFRE | VALOR TOTAL | MTVO  |  UNIDAD  |  VALOR CHEQUE  |  VALOR IVA  |  SALDO ANTERIOR  |  GMF | IND");
				Reporter.write(" ");

				// Retorna los movimientos en Stratus
				for (MovimientoStratus movimiento : datosCtadh2) {
					DXCUtil.waitMilisegundos(125);
					String oficinaStratus = null, tipoStratus = null, mtvoStratus = null, talon = null;
					double ValorTotalMov = 0.0, ValorChequeMovStartus = 0.0;
					double saldoAnteriorStratus = 0.0;
					double unidad = 0.0;

					String movToString = movimiento.toString().replace("[INDIC:", "").replace("]", "")
							.replace("SALDO:", "|").replace("UNID :", "|").replace("EMERG:", "|").replace("IVA  :", "|")
							.replace(" ", "").replace("  ", "");

					String[] datos = movToString.split("\\|");// Extraer los datos necesarios por posiciones

					fecha = datos[0].trim();
					String hora = datos[1].trim();
					String term = datos[2].trim();
					talon = datos[3].trim();
					tipoStratus = datos[4].trim();
					oficinaStratus = datos[5].trim();
					String valorStratus = datos[6].trim();
					ValorTotalMov = Double.parseDouble(valorStratus);
					mtvoStratus = datos[7].trim();
					String FechaMovStratus = datos[8].trim();
					String saldoAnterior = datos[9].trim();
					saldoAnteriorStratus = Double.parseDouble(saldoAnterior);
					String saldoUnidad = datos[10].trim();
					unidad = Double.parseDouble(saldoUnidad);
					String chequeStratus = datos[11];
					String in = datos[12];
					String valorEmerg = datos[13];
					
					if (!oficinaStratus.contains("4636")&&!oficinaStratus.contains("4522")&&!oficinaStratus.contains("4863")) {
					String indCancelado = datos[14];
					String ivam = datos[15];

					String msgcancelado = " ";

					if (indCancelado != null && indCancelado.equals("true")) {
						msgcancelado = "Cancelado";
					}

						
					Reporter.write("Movimiento: " + fecha + " | " + hora + " | " + talon + " | " + tipoStratus + "  |  "
							+ oficinaStratus + " | " + BigDecimal.valueOf(ValorTotalMov) + " | " + mtvoStratus + "  |  "
							+ BigDecimal.valueOf(unidad) + "  |  " + BigDecimal.valueOf(ValorChequeMovStartus) + "  |  "
							+ ivam + "  |  " + saldoAnterior + "  |  " + valorEmerg + " | " + msgcancelado);

					String movr = fecha + " " + hora + " " + talon + " " + tipoStratus + " " + oficinaStratus + " "
							+ BigDecimal.valueOf(ValorTotalMov) + " " + mtvoStratus + " " + BigDecimal.valueOf(unidad)
							+ " " + BigDecimal.valueOf(ValorChequeMovStartus) + " " + ivam + " " + saldoAnterior + " "
							+ valorEmerg + " " + indCancelado;

					if (movr.contains("0034") || movr.contains("0055") || movr.contains("0041")) {
						movimientosStratus.add(movr.split(","));
					}
					}

				}

				double sumaMovimientos = 0.0;
				double sumaunida = 0.0;
				double sumaCheque = 0.0;
				double sumaIva = 0.0;
				double sumaGmf = 0.0;
				double saldoTotalFinalEsperado = 0.0;

				int posicionSaldoInicialpos = -1;
				int posicionSaldoInicialpos2 = -1;
//				String servicio = SettingsRun.getTestData().getParameter("Servicio").trim();
				switch (servicio) {

				case "Nómina":
				case "Pago de Nómina":
				case "Pago de Nóminas":
				case "Pago a Proveedores":
				case "Pagos a proveedores":
				case "Pagos proveedores":
				case "Proveedores":
				case "AFC":
				case "Pago a Créditos de Terceros":
				case "Pagos a créditos de terceros":
				case "Crédito.3ros":
				case "Pago de Servicios":
				case "Servicios":
				case "Transferencias Cuenta Inscrita":
				case "Cuenta Inscrita":
				case "Transferencias Cuenta No Inscrita":
				case "Transferencias Cuenta NO Inscrita":
				case "Cuenta No Inscrita":
				case "Transferencias Mismo NIT":
				case "Transferencia NIT Propio":
				case "Mismo NIT":
					// Encuentra la posición del saldo inicial en movimientosStratus
					double saldoInipo = 0.0;
					for (int i = 0; i < movimientosStratus.size(); i++) {
						String[] movimiento = movimientosStratus.get(i);
						String[] datosMovIni = movimiento[0].split(" ");

						if (movimiento[movimiento.length - 1].contains(saldoPos.substring(0, saldoPos.length() - 3))) {
							posicionSaldoInicialpos = i;
							saldoInipo = Double.parseDouble(datosMovIni[10].trim().replace(",", ""));
							break;
						}
					}

					for (int j = 0; j < movimientosStratus.size(); j++) {
						String[] movimiento = movimientosStratus.get(j);
						if (movimiento.length > 1)
							if (movimiento[movimiento.length - 1]
									.contains(saldoPosFinal.substring(0, saldoPosFinal.length() - 3))) {
								posicionSaldoInicialpos2 = j;
								break;
							}

						if (posicionSaldoInicialpos2 != -1 && posicionSaldoInicialpos < posicionSaldoInicialpos2) {
							posicionSaldoInicialpos = posicionSaldoInicialpos2;
						}

						if (posicionSaldoInicialpos2 == -1) {
							double saldoFinalPo = 0.0;
							posicionSaldoInicialpos2 = movimientosStratus.size() - 2;

							if (posicionSaldoInicialpos2 > 0) {
								String[] movimiento1 = movimientosStratus.get(posicionSaldoInicialpos2);
								if (movimiento1.length > 0) {
									String[] datosMov = movimiento1[0].split(" ");
									if (datosMov.length > 0) {
										saldoFinalPo = Double.parseDouble(datosMov[10].trim().replace(",", ""));
										if (saldoFinalPo < saldoInipo) {
											posicionSaldoInicialpos = posicionSaldoInicialpos2;
										}
									}
								}
							}
						}
					}

					break;

//				case "Mismo NIT":
//				posicionSaldoInicialpos = movimientosStratus.size() - 2;

//				break;
				case "Pagos Propios":
					break;

				}

				Reporter.write(" ");
				Reporter.write("*** Valores Cobros, IVA y GMF");
				Reporter.write(" ");

				String porGmf = DatosDavivienda.STRATUS.getGMF();

				if (porGmf == null) {
					porGmf = "1.0040";
				}

				double cobrovalorIva = 0.0;
				double cobrovalorGMF = 0.0;
				if (posicionSaldoInicialpos != -1) {

					// Sumar todos los movimientos, IVA y GMF
					for (int i = posicionSaldoInicialpos; i < movimientosStratus.size(); i++) {
						String[] movimiento = movimientosStratus.get(i);
						String[] datosMov = movimiento[0].split(" ");

						double valorMovimiento = Double.parseDouble(datosMov[5].trim().replace(",", ""));
						String tipoMovimiento = datosMov[6].trim();
						double valorunida = Double.parseDouble(datosMov[7].trim().replace(",", ""));
						double valorCheque = Double.parseDouble(datosMov[8].trim().replace(",", ""));
						double valorIva = Double.parseDouble(datosMov[9].trim().replace(",", ""));
						double emerg = Double.parseDouble(datosMov[11].trim().replace(",", ""));
						String cancelados = datosMov[12].trim();

						sumaMovimientos += valorMovimiento;
						sumaunida += valorunida;
						sumaCheque += valorCheque;
						sumaIva += Math.round(valorIva);
						sumaGmf += emerg;

						if (valorIva != 0) {

							Reporter.write("Valor Cobro Iva Movimiento: " + BigDecimal.valueOf(valorIva));
							cobrovalorIva = (valorMovimiento * 19 / 100);
							Reporter.write("Compara el Valor del Iva  : " + BigDecimal.valueOf(cobrovalorIva));

							cobrovalorIva = valorIva;

						} else if (valorIva == 0 && valorunida != 0) {
//						cobrovalorIva = (valorunida * 19 / 100);
							Reporter.write("Para los movimientos cobros o 6 No se realiza la suma del Iva");
							Reporter.write("Posible Valor Cobro Iva que podria aparecer: "
									+ BigDecimal.valueOf((valorunida * 19 / 100)));
						} else if (valorIva == 0 && valorunida == 0) {
							cobrovalorIva = (valorunida * 19 / 100);
							Reporter.write("Valor Cobro Iva: " + BigDecimal.valueOf(cobrovalorIva));
						}

						if (valorunida != 0) {
							if (valorIva == 0) {

								if (porGmf.contains("1.0040")) {
									Reporter.write(
											"Valor Cobro  GMF1: " + BigDecimal.valueOf((valorMovimiento / 1000 * 4)));
									cobrovalorGMF = (valorMovimiento / 1000 * 4);
								} else if (porGmf.contains("1.0020")) {
									Reporter.write(
											"Valor Cobro  GMF1: " + BigDecimal.valueOf((valorMovimiento / 1000 * 2)));
									cobrovalorGMF = (valorMovimiento / 1000 * 2);
								} else if (porGmf.contains("1.0030")) {
									Reporter.write(
											"Valor Cobro  GMF1: " + BigDecimal.valueOf((valorMovimiento / 1000 * 3)));
									cobrovalorGMF = (valorMovimiento / 1000 * 3);
								} else if (porGmf.contains("1.0035")) {
									Reporter.write(
											"Valor Cobro  GMF1: " + BigDecimal.valueOf((valorMovimiento / 1000 * 35)));
									cobrovalorGMF = (valorMovimiento / 1000 * 35);
								}
							} else if (valorIva != 0) {
								double cobrosuma = (valorMovimiento + cobrovalorIva);
								if (porGmf.contains("1.0040")) {

									Reporter.write("Valor Cobro  GMF2: " + BigDecimal.valueOf((cobrosuma / 1000 * 4)));
									cobrovalorGMF = (cobrosuma / 1000 * 4);
								} else if (porGmf.contains("1.0020")) {
									Reporter.write("Valor Cobro  GMF2: " + BigDecimal.valueOf((cobrosuma / 1000 * 2)));
									cobrovalorGMF = (cobrosuma / 1000 * 2);
								} else if (porGmf.contains("1.0030")) {
									Reporter.write("Valor Cobro  GMF2: " + BigDecimal.valueOf((cobrosuma / 1000 * 3)));
									cobrovalorGMF = (cobrosuma / 1000 * 3);
								} else if (porGmf.contains("1.0035")) {
									Reporter.write("Valor Cobro  GMF2: " + BigDecimal.valueOf((cobrosuma / 1000 * 35)));
									cobrovalorGMF = (cobrosuma / 1000 * 35);
								}
							}
						} else if (valorunida == 0) {
							double cobrosuma = (valorMovimiento + cobrovalorIva);
							if (porGmf.contains("1.0040")) {
								Reporter.write("Valor Cobro  GMF: " + BigDecimal.valueOf((cobrosuma / 1000 * 4)));
								cobrovalorGMF = (cobrosuma / 1000 * 4);
							} else if (porGmf.contains("1.0020")) {
								Reporter.write("Valor Cobro  GMF: " + BigDecimal.valueOf((cobrosuma / 1000 * 2)));
								cobrovalorGMF = (cobrosuma / 1000 * 2);
							} else if (porGmf.contains("1.0030")) {
								Reporter.write("Valor Cobro  GMF: " + BigDecimal.valueOf((cobrosuma / 1000 * 3)));
								cobrovalorGMF = (cobrosuma / 1000 * 3);
							} else if (porGmf.contains("1.0035")) {
								Reporter.write("Valor Cobro  GMF: " + BigDecimal.valueOf((cobrosuma / 1000 * 35)));
								cobrovalorGMF = (cobrosuma / 1000 * 35);
							}
						}

						if (valorIva != 0) {
							double cobrosuma = (valorMovimiento + cobrovalorIva);

							Reporter.write("Valor de la Tx: " + BigDecimal.valueOf(valorMovimiento)
									+ " Mas el Valor IVA: " + BigDecimal.valueOf(cobrovalorIva) + " La Suma es de: "
									+ BigDecimal.valueOf(cobrosuma));

							Reporter.write(" ");

							Reporter.write("Valor Unidad: " + (cobrosuma + cobrovalorGMF));
						}

						if (valorIva == 0) {
							double cobrosuma = (valorMovimiento + cobrovalorGMF);
							Reporter.write("Valor de la Tx: " + BigDecimal.valueOf(valorMovimiento)
									+ " Mas el Valor Gmf: " + BigDecimal.valueOf(cobrovalorGMF) + " La Suma es de: "
									+ BigDecimal.valueOf(cobrosuma));

							Reporter.write(" ");
							Reporter.write("Valor Unidad: " + cobrosuma);
						}

						Reporter.write(" ");

					}

					double restaMovimientos = 0.0;
					double restaunida = 0.0;
					double restaCheque = 0.0;
					double restaIva = 0.0;
					double restaGmf = 0.0;
					String cancelados = "false";
					int canNumCancelados = 0;
					// Restar movimientos cancelados y tipoMovimiento 307
					for (int i = posicionSaldoInicialpos; i < movimientosStratus.size(); i++) {
						String[] movimiento = movimientosStratus.get(i);
						String[] datosMov = movimiento[0].split(" ");

						double valorMovimiento = Double.parseDouble(datosMov[5].trim().replace(",", ""));
						String tipoMovimiento = datosMov[6].trim();
						double valorunida = Double.parseDouble(datosMov[7].trim().replace(",", ""));
						double valorCheque = Double.parseDouble(datosMov[8].trim().replace(",", ""));
						double valorIva = Double.parseDouble(datosMov[9].trim().replace(",", ""));
						double emerg = Double.parseDouble(datosMov[11].trim().replace(",", ""));
						cancelados = datosMov[12].trim();

						if (tipoMovimiento.equals("307") || tipoMovimiento.equals("34")
								|| tipoMovimiento.equals("741")) {
							restaMovimientos += valorMovimiento;
							restaunida += valorunida;
							restaCheque += valorCheque;
							restaIva += valorIva;
							restaGmf += emerg;
							canNumCancelados++;
						} else if (tipoMovimiento.equals("307") || tipoMovimiento.equals("34")
								|| tipoMovimiento.equals("741") || cancelados.equals("true")) {
							restaMovimientos += valorMovimiento;
							restaunida += valorunida;
							restaCheque += valorCheque;
							restaIva += Math.round(valorIva);
							restaGmf += emerg;
							canNumCancelados++;
						}
					}

					double valorMov = 0.0;
					valorMov = (sumaMovimientos - restaMovimientos);

					double valorUnidad = 0.0;
					valorUnidad = (sumaunida - restaunida);

					double valorCheque = 0.0;
					valorCheque = (sumaCheque - restaCheque);

					double valorIva = 0.0;
					valorIva = (sumaIva - restaIva);

					double valorGmf = 0.0;
					valorGmf = (sumaGmf - restaGmf);

					Reporter.write(" ");
					Reporter.write("*** VALIDACIÓN DEL MOVIMIENTO");
					Reporter.write(" ");

					Reporter.write("SUMA de los Movimientos: " + BigDecimal.valueOf(sumaMovimientos));
					Reporter.write("SUMA de las Unidades: " + BigDecimal.valueOf(sumaunida));
					Reporter.write("SUMA de los Cheques: " + BigDecimal.valueOf(sumaCheque));
					Reporter.write("SUMA de los Iva: " + BigDecimal.valueOf(sumaIva));
					Reporter.write("SUMA de los GMF: " + BigDecimal.valueOf(sumaGmf));
					Reporter.write(" ");

					Reporter.write("Total de Movimientos Cancelados: " + canNumCancelados);

					if (cancelados.equals("true")) {

						Reporter.write("SUMA de los Movimiento cancelado: " + BigDecimal.valueOf(restaMovimientos));
						Reporter.write("SUMA de los Unidad cancelado: " + BigDecimal.valueOf(restaunida));
						Reporter.write("SUMA de los Cheque cancelado: " + BigDecimal.valueOf(restaCheque));
						Reporter.write("SUMA de los Iva cancelado: " + BigDecimal.valueOf(restaIva));
						Reporter.write("SUMA de los GMF cancelado: " + BigDecimal.valueOf(restaGmf));
						Reporter.write(" ");
					}

					double costoTx = 0.0;
					costoTx = (sumaMovimientos + restaMovimientos);

					// Restar los montos al saldo inicial
					double saldoesperado = 0.0;

					if (valorUnidad != 0) {
						saldoesperado = (saldoTotalInicial - valorUnidad - valorCheque);
					} else if (valorUnidad == 0) {
						saldoesperado = (saldoTotalInicial - valorMov - Math.round(valorIva) - valorGmf);

					}

					Reporter.write(" ");
					Reporter.write("*** HACIENDO LAS VALIDACIÓN DEL MOVIMIENTO");
					Reporter.write(" ");
					Reporter.write("Valor del Movimiento: " + BigDecimal.valueOf(valorMov));
					Reporter.write("Valor del Unidad: " + BigDecimal.valueOf(valorUnidad));
					Reporter.write("Valor del Cheque " + BigDecimal.valueOf(valorCheque));

					Reporter.write("Valor del Iva: " + BigDecimal.valueOf(valorIva));
					Reporter.write("Valor del GMF: " + BigDecimal.valueOf(valorGmf));

					saldoTotalFinalEsperado = saldoesperado;

					// Calcular la diferencia entre los saldos finales
					double diferencia = 0.0;

					diferencia = (saldoTotalFinal - saldoTotalFinalEsperado);
					String saldoEsperado = String.format("%.1f", saldoTotalFinalEsperado);
					Reporter.write(" ");
					// Mostrar los resultados
					Reporter.reportEvent(Reporter.MIC_HEADER, "INFRORMACIÓN DE SALDO FINAL");
					Reporter.write(" ");
					Reporter.reportEvent(Reporter.MIC_INFO, "El Saldo Final Stratus : " + saldofinal);

					Reporter.reportEvent(Reporter.MIC_INFO,
							"El Saldo Final esperado: " + BigDecimal.valueOf(saldoTotalFinalEsperado));

					Reporter.write(" ");
					Reporter.reportEvent(Reporter.MIC_HEADER, "DIFERENCIAL ENTRE LOS SALDOS");
					Reporter.write(" ");

					Reporter.reportEvent(Reporter.MIC_INFO,
							"La diferencia entre los saldos final es: " + BigDecimal.valueOf(Math.round(diferencia)));

				} else {

					Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontró el saldo inicial en los movimientos.");

				}
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "POR FAVOR VERIFICAR DATOS DE CONSULTA");
			}
		}
	}

	/**
	 * Almacena información para ser utilizada en cobros y saber el destino al cual
	 * hace referencia el ripode transacción
	 * 
	 * @return
	 */
	public String[] estadoFinalTx() {
		return registrosmov;
	}

//=======================================================================================================================================
	/***
	 * Metodo Realiza la Consulta de desde la lista o desde la ruta menu
	 * 
	 * @return
	 * @throws Exception
	 */
	public String SaldosConsolidado(String navegador,String product,String tipoConsul, boolean listaconsulta, String tipoProduct, String numeroProducto)
			throws Exception {
		this.pageOrigen = new PageOrigen(this);
		int contador = 0;
		do {
			DXCUtil.wait(1);
			contador++;

			String msgError = this.getMsgAlertIfExist("lblMasterAlerta");

			if (msgError != null && !msgError.equals(" ")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
				Evidence.saveFullPage(msgError, this);
				this.pageOrigen.terminarIteracion();
			}

			if (contador > 30) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro los saldos consalidados");
				this.pageOrigen.terminarIteracion();
			}
		} while (this.element(TitulosaldosConsolidados) == null);
		Evidence.saveFullPage("SaldosConsolidado", this);

		if (listaconsulta) {
			String xPathRB = saldosConsolidadosFinanciacion.replace("NUM_PROD", DXCUtil.right(numeroProducto, 4)).replace("TIPO_PROD", tipoProduct);
			if (this.element(xPathRB) == null && tipoProduct.equals("Cafetera"))
				 xPathRB = saldosConsolidadosFinanciacion.replace("NUM_PROD", DXCUtil.right(numeroProducto, 4)).replace("TIPO_PROD", "cafetera");
			if (this.element(xPathRB) == null && tipoProduct.equals("Superior"))
				xPathRB = saldosConsolidadosFinanciacion.replace("NUM_PROD", DXCUtil.right(numeroProducto, 4)).replace("TIPO_PROD", "superior");
			
		
			WebElement objRadioButtonProd = this.element(xPathRB);
			do {
				DXCUtil.wait(1);
				contador++;

				String msgError = this.getMsgAlertIfExist("lblMasterAlerta");

				if (msgError != null && !msgError.equals(" ")) {
					Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
					Evidence.saveFullPage(msgError, this);
					this.pageOrigen.terminarIteracion();
				}

				if (contador > 30) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro la lista desplegable de las cuentas");
					this.pageOrigen.terminarIteracion();
				}
			} while (this.element(objRadioButtonProd) == null);
			
			if (objRadioButtonProd != null) {
				String msg = null;
				if (tipoConsul.contains("DESCARGA")) {

					msg = this.selectListItem(objRadioButtonProd, "Movimientos y Extractos");
				} else if (!tipoConsul.contains("DESCARGA")) {
//					String product = SettingsRun.getTestData().getParameter("Producto").trim();
					if (product.equals("FAMILIA FONDOS")) {
						tipoConsul = "Saldo Familia de Fondos";
					}
					msg = this.selectListItem(objRadioButtonProd, tipoConsul);
				}
				Evidence.saveFullPage("ProdConsulta", this);
				if (msg != null && !msg.equals("")) {
					return msg;
				}
			} else if (objRadioButtonProd == null) {
				return "No se encontro el tipo de consulta:" + tipoConsul + "Con la cuenta: " + tipoProduct + " - " + DXCUtil.right(numeroProducto, 4);
			}

		} else if (!listaconsulta) {
			String[] ruta = this.RutaConsulta(tipoConsul);
			this.pageOrigen = new PageOrigen(this);
			do {
				contador++;
				DXCUtil.wait(1);

				String msgError = this.getMsgAlertIfExist("lblMasterAlerta");

				if (msgError != null && !msgError.equals(" ")) {
					Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
					Evidence.saveFullPage(msgError, this);
					this.pageOrigen.terminarIteracion();
				}

				if (contador > 30) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro el Modulo: " + ruta);
					this.pageOrigen.terminarIteracion();
				}
			} while (this.element(MenuModu) == null);

			String texto = this.getText(this.element(MenuModu));

			if (texto.contains("Consultas y Extractos")) {
				texto = "Consultas y Extractos";
			} else if (texto.contains("Consultas")) {
				texto = "Consultas";

			}

//			String navegador = SettingsRun.getTestData().getParameter("Navegador").trim();
			if (navegador.contains("CHROME")) {
				String msgError = pageOrigen.irAOpcion(ruta[0], texto, ruta[1]);
			} else {
				String msgError = pageOrigen.irAOpcionMoZilla(ruta[0], texto, ruta[1], null, null);

			}

		}
		return null;
	}

//=======================================================================================================================================

	public String[] RutaConsulta(String tipoconsul) {
		String[] ruta = null;

		if (tipoconsul.contains("SALDOS")) {
			ruta = new String[] { "Detalle de Saldos", "Detalle de Saldos por Producto" };
		} else if (tipoconsul.contains("MOVIMIENTOS")) {
			ruta = new String[] { "Movimientos y Extractos por Producto", "Movimientos y Extractos" };
		} else if (tipoconsul.contains("CHEQUERAS")) {
			ruta = new String[] { "Cheques", "Chequeras" };
		} else if (tipoconsul.contains("CHEQUES DEVUELTOS")) {
			ruta = new String[] { "Cheques Devueltos", "Cheques Devueltos" };

		} else if (tipoconsul.contains("TX A TRAVÉS PORTAL") || tipoconsul.contains("DESCARGA TX A TRAVÉS")) {
			ruta = new String[] { "Transacciones Realizadas en el Portal PYME", "Transacciones a Través del Portal" };
		}

		return ruta;
	}

//=======================================================================================================================================

	public String[] MovimientosPorProducto(String tipoConsul,String tipoProd,String tiempoconsulta,String tipoProducto, String numeroProducto, boolean Descarga,String descargarArch)
			throws Exception {
		this.pageOrigen = new PageOrigen(this);
		Date fecha = new Date();
		boolean verdadero1 = false;
		int contadorSiguiente = 0;
		int contador = 0;
		int contad = 0;

//		this.tiempoconsulta = SettingsRun.getTestData().getParameter("Tiempo de Consulta").trim();
		DXCUtil.wait(5);
		do {
			contador++;
			DXCUtil.wait(1);
			String msgError = this.getMsgAlertIfExist("lblMasterAlerta");

			if (this.element(ListProduc) == null && msgError != null && !msgError.equals(" ")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
				Evidence.saveFullPage(msgError, this);
				this.pageOrigen.terminarIteracion();
			}
			
			if (contador > 30) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro el campo de Produtos");
				this.pageOrigen.terminarIteracion();
			}
		} while (this.element(ListProduc) == null);

		Evidence.save("ListaProdutos",this);
		
		if (tipoProducto.equals("Cafetera")) {
			tipoProducto = "Tarjetas cafetera";
		}
		
		String msgError = this.lisProducto(ListProduc, tipoProducto, numeroProducto);
		
		if (msgError != null) {
			Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
			this.pageOrigen.terminarIteracion();
		}

		if (this.element(btnBusqAvanzada) != null) {

			do {
				DXCUtil.wait(1);
			} while (this.element(btnBusqAvanzada) == null);
		}

		if (tiempoconsulta.contains("más criterios")) {

			Date fechaDesdes = DXCUtil.dateAdd(fecha, Calendar.DAY_OF_MONTH, -30); // Fecha inicial
			this.todaydesde = DXCUtil.dateToString(fechaDesdes, "dd/mm/yyyy");

			this.fechahasta = DXCUtil.dateToString(fecha, "dd/mm/yyyy"); // Fecha final
			if (this.element(btnBusqAvanzada) != null) {
				this.click(this.element(btnBusqAvanzada));
			}
			DXCUtil.wait(2);
			this.click(this.element(btnMostDatos));
			this.waitIngresoModulo("Búsqueda Avanzada de Movimientos");

			do {
				contador++;
				DXCUtil.wait(1);
				msgError = this.getMsgAlertIfExist("lblMasterAlerta");

				if (msgError != null && !msgError.equals(" ")) {
					Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
					Evidence.saveFullPage(msgError, this);
					this.pageOrigen.terminarIteracion();
				}

				if (contador > 30) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro el campo de listado");
					this.pageOrigen.terminarIteracion();
				}

			} while (this.element(ListProduc2) == null);

			msgError = this.lisProducto(ListProduc2, tipoProducto, numeroProducto);

			if (msgError != null) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
				this.pageOrigen.terminarIteracion();
			}

			this.write(cmpFechadesde, this.todaydesde);
			this.write(cmpFechaInicio, this.fechahasta);

			this.click(this.element(btnMostDatos));

		} else if (tiempoconsulta.contains("48 horas")) {

			Date fechaDesdes = DXCUtil.dateAdd(fecha, Calendar.HOUR_OF_DAY, -48); // Fecha inicial
			this.todaydesde = DXCUtil.dateToString(fechaDesdes, "dd/mm/yyyy");

			this.fechahasta = DXCUtil.dateToString(fecha, "dd/mm/yyyy"); // Fecha final

			this.click(this.element(btn48));
			DXCUtil.wait(1);
			do {
				contador++;

				DXCUtil.wait(1);
				msgError = this.getMsgAlertIfExist("lblMasterAlerta");

				if (msgError != null && !msgError.equals(" ")) {
					Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
					Evidence.saveFullPage(msgError, this);
					this.pageOrigen.terminarIteracion();
				}

				if (contador > 30) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro el boton de Datos");
					this.pageOrigen.terminarIteracion();
				}

			} while (this.element(btnMostDatos) == null);
			this.click(this.element(btnMostDatos));

		} else if (tiempoconsulta.contains("7 días")) {

			Date fechaDesdes = DXCUtil.dateAdd(fecha, Calendar.DAY_OF_MONTH, -7); // Fecha inicial
			this.todaydesde = DXCUtil.dateToString(fechaDesdes, "dd/mm/yyyy");

			this.fechahasta = DXCUtil.dateToString(fecha, "dd/mm/yyyy"); // Fecha final

			this.click(this.element(btn7));
			DXCUtil.wait(1);
			this.click(this.element(btnMostDatos));

		} else if (tiempoconsulta.contains("30 días")) {

			Date fechaDesdes = DXCUtil.dateAdd(fecha, Calendar.DAY_OF_MONTH, -30); // Fecha inicial
			this.todaydesde = DXCUtil.dateToString(fechaDesdes, "dd/mm/yyyy");

			this.fechahasta = DXCUtil.dateToString(fecha, "dd/mm/yyyy"); // Fecha final

			this.click(this.element(btn30));
			DXCUtil.wait(1);
			this.click(this.element(btnMostDatos));

		}

		do {
			DXCUtil.wait(1);
			contador++;
			msgError = this.getMsgAlertIfExist("lblMasterAlerta");

			if (msgError != null && !msgError.equals(" ")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
				Evidence.saveFullPage(msgError, this);
				this.pageOrigen.terminarIteracion();
			}

			if (contador > 30) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro la Taba de los Movimientos");
				this.pageOrigen.terminarIteracion();
			}
		} while (this.element(table) == null);

		String[] lismov = null;

		if (this.element(btnSiguiente) == null) {

			Evidence.saveFullPage("Movimientos", this);
			
//			String tipoConsul = SettingsRun.getTestData().getParameter("Tipo de Consulta").trim();
//			String tipoProd = SettingsRun.getTestData().getParameter("Tipo producto a Consultar").trim();
			
			if (!tipoConsul.contains("DESCARGA MOVIMIENTOS") && !tipoProd.contains("Daviplus")) {

				lismov = this.TabelMovi();
			}
			if (tipoProd.contains("Daviplus")) {

				lismov = this.TabelMovi2();
			}

		} else {

			List<String> moviList = new ArrayList<>(); // Use a List to accumulate data from all pages
			boolean ver;
			do {
				contadorSiguiente++;
				DXCUtil.wait(1);
				Evidence.saveFullPage("Movimientos pag" + contadorSiguiente, this);

				// Captura datos de la página actual y agrégalos al list
				String[] currentPageData = this.TabelMovi();
				moviList.addAll(Arrays.asList(currentPageData));

				DXCUtil.wait(10);

				if (this.element(btnSiguiente) != null) {

					this.click(btnSiguiente);

					ver = true;

				} else {
					ver = false;
				}

			} while (ver != false);

			// Convert the List to an array
			lismov = moviList.toArray(new String[0]);
		}

		if (Descarga) {

			do {
				contador++;
				DXCUtil.wait(1);
				msgError = this.getMsgAlertIfExist("lblMasterAlerta");

				if (msgError != null && !msgError.equals(" ")) {
					Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
					Evidence.saveFullPage(msgError, this);
					this.pageOrigen.terminarIteracion();
				}

				if (contador > 30) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro el boton de descargas");
					this.pageOrigen.terminarIteracion();
				}
			} while (this.element(btnDesArch) == null);

//			String descargarArch = SettingsRun.getTestData().getParameter("Formato de Descarga").trim();

			msgError = this.selectListItem2(this.element(btnseleArch), descargarArch);
			Evidence.saveFullPage("Des", this);

			if (!msgError.isEmpty()) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
				this.pageOrigen.terminarIteracion();
			}

			DXCUtil.wait(8);

			if (this.element(nomArchDes) != null && this.isEnabled(this.element(nomArchDes))) {
				String descargaArch = "DescargarMovimiento" + SettingsRun.getCurrentIteration();
				this.write(this.element(nomArchDes), descargaArch);
			}

			do {
				contador++;
				DXCUtil.wait(1);
				msgError = this.getMsgAlertIfExist("lblMasterAlerta");

				if (msgError != null && !msgError.equals(" ")) {
					Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
					Evidence.saveFullPage(msgError, this);
					this.pageOrigen.terminarIteracion();
				}

				if (contador > 30) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro el boton de descarga");
					this.pageOrigen.terminarIteracion();
				}
			} while (this.element(btnDescargaMasiva) == null);

			this.click(this.element(btnDescargaMasiva));

			DXCUtil.wait(6);

			Evidence.saveFullPage("descarga", this);
			return null;
		}

		return lismov;
	}

//=======================================================================================================================================

	public String[] today() {
		String[] today = { this.todaydesde, this.fechahasta };
		return today;
	}

// =======================================================================================================================================

	public void ResumenDeprodutos() throws Exception {
		this.pageOrigen = new PageOrigen(this);
		int contador = 0;
		do {
			DXCUtil.wait(10);
			contador++;
			if (contador > 7) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro el :");
				this.pageOrigen.terminarIteracion();
			}
		} while (this.element(TitulosaldosConsolidados) == null);
		Evidence.saveFullPage("SaldosConsolidado", this);

		String valor = "0,00";
		double totalBalance = 0;
		double val = 0.00;
		double ahorros = 0.00;
		double corriente = 0.00;
		double recaudolibranza = 0.00;
		double Refcredito = 0.00;
		double crediplus = 0.00;
		double certificados = 0.00;
		double daviplusrentaFijaPesos = 0.00;

		// Iterar a través de filas en la primera tabla1
		WebElement accountsTable = this.element(tabla1ahorroeinversiones);

		for (WebElement row : accountsTable.findElements(By.className("CeldaGrilla"))) {
			// Extraer datos de cada fila
			String accountType = row.findElement(By.xpath(".//td[1]")).getText().trim();
			String balance = row.findElement(By.xpath(".//td[3]")).getText().trim();

			if (accountType == null || !accountType.equals("")) {
				// Maneja el caso donde está presente "Producto BLOQUEADO"
				if (balance.contains("BLOQUEADO")) {
					balance = valor; // If "Producto BLOQUEADO," consider the balance as 0.0
				}

				if (balance != null) {

					// Convierte el saldo a doble y lo suma al total
					try {
						totalBalance = Double.parseDouble(balance.replace("$", "").replace(",", "").replace(".", ""));
					} catch (NumberFormatException e) {
						// Manejar el caso donde el saldo no es un valor numérico válido
						// Por ejemplo, establezca totalBalance en 0,00 o manéjelo de otra manera
						totalBalance = val;
					}
				}

				// Genera el tipo de cuenta y el saldo (para fines de verificación)
				if (accountType.equals("Cuenta de ahorros")) {
					ahorros += totalBalance;
				} else if (accountType.equals("Cuenta corriente")) {
					corriente += totalBalance;
				} else if (accountType.contains("recaudo") && accountType.contains("libranza")) {
					recaudolibranza += totalBalance;
				} else if (accountType.contains("Referencia") && accountType.contains("crédito")) {
					Refcredito += totalBalance;
				} else if (accountType.equals("Crédiplus")) {
					crediplus += totalBalance;
				} else if (accountType.equals("Certificados")) {
					certificados += totalBalance;
				} else if (accountType.contains("Daviplus") && accountType.contains("Renta")
						&& accountType.contains("Fija") && accountType.contains("Pesos")) {
					daviplusrentaFijaPesos += totalBalance;
				}
			}
		}

		double ahorros2 = 0.00;
		double corriente2 = 0.00;
		double tarjetas = 0.00;
		double crediexpress = 0.00;
		double constructor = 0.00;
		double corporativo = 0.00;
		double leasinghabitacional = 0.00;
		double credito = 0.00;
		double redescuentoFinagro = 0.00;

		// Iterar a través de filas en la primera tabla2
		WebElement accountsTable2 = this.element(tabla2financiación);
		for (WebElement row : accountsTable2.findElements(By.className("CeldaGrilla"))) {
			// Extraer datos de cada fila
			String accountType = row.findElement(By.xpath(".//td[1]")).getText().trim();
			String balance = row.findElement(By.xpath(".//td[3]")).getText().trim();

			if (accountType == null || !accountType.equals("")) {

				if (balance != null) {
					// Maneja el caso donde está presente "Producto BLOQUEADO"
					if (balance.contains("BLOQUEADO")) {
						balance = valor; // If "Producto BLOQUEADO," consider the balance as 0.0
					}
					// Convierte el saldo a doble y lo suma al total
					try {
						totalBalance = Double.parseDouble(balance.replace("$", "").replace(",", "").replace(".", ""));
					} catch (NumberFormatException e) {
						// Manejar el caso donde el saldo no es un valor numérico válido
						// Por ejemplo, establezca totalBalance en 0,00 o manéjelo de otra manera
						totalBalance = val;
					}
				}

				if (accountType.equals("Cuenta de ahorros")) {
					ahorros2 += totalBalance;
				} else if (accountType.equals("Cuenta corriente")) {
					corriente2 += totalBalance;
				} else if (accountType.contains("Tarjetas") || accountType.contains("Tarjeta ")
						|| accountType.contains("agropecuaria") || accountType.contains("visa")
						|| accountType.contains("mastercard") || accountType.contains("diners")) {
					tarjetas += totalBalance;
				} else if (accountType.contains("Crediexpress")) {
					crediexpress += totalBalance;
				} else if (accountType.contains("constructor")) {
					constructor += totalBalance;
				} else if (accountType.contains("corporativo")) {
					corporativo += totalBalance;
				} else if (accountType.contains("Leasing") && accountType.contains("habitacional")) {
					leasinghabitacional += totalBalance;
				} else if (accountType.equals("Crédito")) {
					credito += totalBalance;
				} else if (accountType.contains("Redescuento") && accountType.contains("Finagro")) {
					redescuentoFinagro += totalBalance;
				}
			}
		}

		double ahorroscom = 0.00;
		double corrientecom = 0.00;
		double Refcredito3 = 0.00;
		double crediplus3 = 0.00;
		double certificados3 = 0.00;
		double daviplusrentaFijaPesos3 = 0.00;
		double recaudolibranza3 = 0.00;

		// Iterar a través de filas en la primera tabla3 Resultado
		WebElement summaryTable3 = this.element(tabla3ahorroeinversiones);
		for (WebElement row : summaryTable3.findElements(By.className("CeldaGrilla"))) {
			// Extraer datos de cada fila
			String accountTypesu = row.findElement(By.xpath(".//td[3]")).getText().trim();
			String totalBalancesu = row.findElement(By.xpath(".//td[4]")).getText().trim();
			// Maneja el caso donde está presente "Producto BLOQUEADO"

			if (!accountTypesu.contains("Total")) {
				// Convierte el saldo a doble y lo suma al total
				try {
					totalBalance = Double
							.parseDouble(totalBalancesu.replace("$", "").replace(",", "").replace(".", ""));
				} catch (NumberFormatException e) {
					// Manejar el caso donde el saldo no es un valor numérico válido
					// Por ejemplo, establezca totalBalance en 0,00 o manéjelo de otra manera
					totalBalance = val;
				}

				if (accountTypesu.equals("Cuenta de ahorros")) {
					ahorroscom = totalBalance;
				} else if (accountTypesu.equals("Cuenta corriente")) {
					corrientecom = totalBalance;
				} else if (accountTypesu.contains("Referencia") && accountTypesu.contains("crédito")) {
					Refcredito3 += totalBalance;
				} else if (accountTypesu.equals("Crédiplus")) {
					crediplus3 += totalBalance;
				} else if (accountTypesu.equals("Certificados")) {
					certificados3 += totalBalance;
				} else if (accountTypesu.contains("Daviplus") && accountTypesu.contains("Renta")
						&& accountTypesu.contains("Fija") && accountTypesu.contains("Pesos")) {
					daviplusrentaFijaPesos3 += totalBalance;
				} else if (accountTypesu.contains("recaudo") && accountTypesu.contains("libranza")) {
					recaudolibranza3 += totalBalance;
				}

			}
		}

		double corriente4 = 0.00;
		double tarjetas4 = 0.00;
		double crediexpress4 = 0.00;
		double constructor4 = 0.00;
		double corporativo4 = 0.00;
		double leasinghabitacional4 = 0.00;
		double credito4 = 0.00;
		double redescuentoFinagro4 = 0.00;

		// Iterar a través de filas en la primera tabla4 Resultado
		WebElement summaryTable4 = this.element(tabla4financiación);
		for (WebElement row : summaryTable4.findElements(By.className("CeldaGrilla"))) {
			// Extraer datos de cada fila
			String accountTypesu = row.findElement(By.xpath(".//td[3]")).getText().trim();
			String totalBalancesu = row.findElement(By.xpath(".//td[4]")).getText().trim();
			// Maneja el caso donde está presente "Producto BLOQUEADO"
			if (!accountTypesu.contains("Total")) {
				// Convierte el saldo a doble y lo suma al total
				try {
					totalBalance = Double
							.parseDouble(totalBalancesu.replace("$", "").replace(",", "").replace(".", ""));
				} catch (NumberFormatException e) {
					// Manejar el caso donde el saldo no es un valor numérico válido
					// Por ejemplo, establezca totalBalance en 0,00 o manéjelo de otra manera
					totalBalance = val;
				}

				if (accountTypesu.equals("Tarjeta de Crédito")) {
					tarjetas4 = totalBalance;
				} else if (accountTypesu.contains("Crédito") && accountTypesu.contains("corporativo")) {
					corporativo4 += totalBalance;
				} else if (accountTypesu.equals("Crediexpress rotativo pyme")) {
					crediexpress4 = totalBalance;
				} else if (accountTypesu.equals("Cuenta corriente")) {
					corriente4 = totalBalance;
				} else if (accountTypesu.contains("constructor")) {
					constructor4 = totalBalance;
				} else if (accountTypesu.contains("corporativo")) {
					constructor4 = totalBalance;
				} else if (accountTypesu.contains("Leasing") && accountTypesu.contains("habitacional")) {
					leasinghabitacional4 += totalBalance;
				} else if (accountTypesu.equals("Crédito")) {
					credito4 += totalBalance;
				} else if (accountTypesu.contains("Redescuento") && accountTypesu.contains("Finagro")) {
					redescuentoFinagro4 += totalBalance;
				} else if (accountTypesu.contains("recaudo") && accountTypesu.contains("libranza")) {
					corrientecom = totalBalance;
				} else if (accountTypesu.equals("Crédito")) {
					credito4 += totalBalance;
				}
			}
		}

		// Compara el saldo total con los valores resumidos

		Reporter.reportEvent(Reporter.MIC_INFO, "Comparación de saldos de los productos");

		Reporter.reportEvent(Reporter.MIC_INFO, "Ahorro e inversiones");

		if (ahorros != 0.00) {
			if (ahorros == ahorroscom) {
				Reporter.reportEvent(Reporter.MIC_PASS,
						" El saldo total de cuentas ahorros coincide con el total resumido. "
								+ BigDecimal.valueOf(ahorros));

			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"El saldo total de cuentas ahorros " + BigDecimal.valueOf(ahorros)
								+ " No coincide con el total resumido. " + BigDecimal.valueOf(ahorroscom));
			}
		}

		if (corriente != 0.00) {
			if (corriente == corrientecom) {
				Reporter.reportEvent(Reporter.MIC_PASS,
						"El saldo total de Cuenta Corriente coincide con el total resumido. "
								+ BigDecimal.valueOf(corriente));
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"El saldo total de Cuenta Corriente " + BigDecimal.valueOf(corriente)
								+ " No coincide con el total resumido. " + BigDecimal.valueOf(corriente));
			}
		}

		if (recaudolibranza != 0.00) {
			if (recaudolibranza == recaudolibranza3) {
				Reporter.reportEvent(Reporter.MIC_PASS,
						"El saldo total de Referencia recaudo coincide libranza con el total resumido. "
								+ BigDecimal.valueOf(recaudolibranza3));
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"El saldo total de Referencia recaudo libranza " + BigDecimal.valueOf(recaudolibranza)
								+ " No coincide con el total resumido. " + BigDecimal.valueOf(recaudolibranza3));
			}
		}

		if (Refcredito != 0.00) {
			if (Refcredito == Refcredito3) {
				Reporter.reportEvent(Reporter.MIC_PASS,
						"El saldo total de Referencia crédito coincide con el total resumido. "
								+ BigDecimal.valueOf(Refcredito3));
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"El saldo total de Referencia crédito " + BigDecimal.valueOf(Refcredito)
								+ " No coincide con el total resumido. " + BigDecimal.valueOf(Refcredito3));
			}
		}

		if (crediplus != 0.00) {
			if (crediplus == crediplus3) {
				Reporter.reportEvent(Reporter.MIC_PASS,
						"El saldo total de Crédiplus coincide con el total resumido." + BigDecimal.valueOf(crediplus3));
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "El saldo total de Crédiplus " + BigDecimal.valueOf(crediplus)
						+ " No coincide con el total resumido. " + BigDecimal.valueOf(crediplus3));
			}
		}

		if (certificados != 0.00) {
			if (certificados == certificados3) {
				Reporter.reportEvent(Reporter.MIC_PASS,
						"El saldo total de Certificados coincide con el total resumido. "
								+ BigDecimal.valueOf(certificados3));
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"El saldo total de Certificados " + BigDecimal.valueOf(certificados)
								+ " No coincide con el total resumido. " + BigDecimal.valueOf(certificados3));
			}
		}

		if (daviplusrentaFijaPesos != 0.00) {
			if (daviplusrentaFijaPesos == daviplusrentaFijaPesos3) {
				Reporter.reportEvent(Reporter.MIC_PASS, "El saldo total de Crédiplus coincide con el total resumido. "
						+ BigDecimal.valueOf(daviplusrentaFijaPesos3));
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"El saldo total de Daviplus Renta Fija Pesos " + BigDecimal.valueOf(daviplusrentaFijaPesos)
								+ " No coincide con el total resumido. " + BigDecimal.valueOf(daviplusrentaFijaPesos3));
			}
		}

		// Compara el saldo total con los valores resumidos

		Reporter.write("Financiación");

		if (tarjetas != 0.00) {
			if (tarjetas == tarjetas4) {
				Reporter.reportEvent(Reporter.MIC_PASS,
						"El saldo total de Tarjetas de Crédito coincide con el resumido. "
								+ BigDecimal.valueOf(tarjetas));
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"El saldo total de Tarjetas de Crédito" + BigDecimal.valueOf(tarjetas)
								+ " No coincide con el resumido. " + BigDecimal.valueOf(tarjetas4));
			}
		}

		if (crediexpress != 0.00) {

			if (crediexpress == crediexpress4) {
				Reporter.reportEvent(Reporter.MIC_PASS,
						"El saldo total de crediexpress coincide con el total resumido. "
								+ BigDecimal.valueOf(crediexpress));
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"El saldo total de crediexpress " + BigDecimal.valueOf(crediexpress)
								+ " No coincide con el total resumido. " + BigDecimal.valueOf(crediexpress4));
			}
		}
		if (constructor != 0.00) {

			if (constructor == constructor4) {
				Reporter.reportEvent(Reporter.MIC_PASS,
						"El saldo total de Crédito constructor coincide con el total resumido. "
								+ BigDecimal.valueOf(constructor));
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"El saldo total de Crédito constructor " + BigDecimal.valueOf(constructor)
								+ " No coincide con el total resumido. " + BigDecimal.valueOf(constructor4));
			}
		}
		if (corporativo != 0.00) {

			if (corporativo == corporativo4) {
				Reporter.reportEvent(Reporter.MIC_PASS,
						"El saldo total de Crédito corporativo coincide con el total resumido. "
								+ BigDecimal.valueOf(corporativo));
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"El saldo total de Crédito corporativo " + BigDecimal.valueOf(corporativo)
								+ " No coincide con el total resumido. " + BigDecimal.valueOf(corporativo4));
			}
		}

		if (corriente2 != 0.00) {
			if (corriente2 == corriente4) {
				Reporter.reportEvent(Reporter.MIC_PASS,
						"El saldo total de Cuenta Corriente coincide con el total resumido. "
								+ BigDecimal.valueOf(corriente2));
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"El saldo total de Cuenta Corriente " + BigDecimal.valueOf(corriente2)
								+ " No coincide con el total resumido. " + BigDecimal.valueOf(corriente4));
			}
		}

		if (leasinghabitacional != 0.00) {
			if (leasinghabitacional == leasinghabitacional4) {
				Reporter.reportEvent(Reporter.MIC_PASS,
						"El saldo total de Leasing habitacional coincide con el total resumido. "
								+ BigDecimal.valueOf(corriente2));
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"El saldo total de Leasing habitacional " + BigDecimal.valueOf(leasinghabitacional)
								+ " No coincide con el total resumido. " + BigDecimal.valueOf(leasinghabitacional4));
			}
		}
		if (credito != 0.00) {
			if (credito == credito4) {
				Reporter.reportEvent(Reporter.MIC_PASS,
						"El saldo total de Crédito coincide con el total resumido. " + BigDecimal.valueOf(credito4));
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "El saldo total de Crédito " + BigDecimal.valueOf(credito)
						+ " No coincide con el total resumido. " + BigDecimal.valueOf(credito4));
			}
		}

		if (redescuentoFinagro != 0.00) {
			if (redescuentoFinagro == redescuentoFinagro4) {
				Reporter.reportEvent(Reporter.MIC_PASS, "El saldo total de Leasing habitacional con el total resumido. "
						+ BigDecimal.valueOf(redescuentoFinagro4));
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"El saldo total de Leasing habitacional " + BigDecimal.valueOf(redescuentoFinagro)
								+ " No coincide con el total resumido. " + BigDecimal.valueOf(redescuentoFinagro4));
			}
		}
		WebElement totalbalancePro = this.element(total1);
		String saldoTotalProductos = totalbalancePro.getText().trim();
		double totalBalancePro = Double
				.parseDouble(saldoTotalProductos.replace("$", "").replace(",", "").replace(".", ""));

		WebElement totalbalancePro2 = this.element(total2);
		String saldoTotalProductosFi = totalbalancePro2.getText().trim();

		double totalBalanceProFi = Double
				.parseDouble(saldoTotalProductosFi.replace("$", "").replace(",", "").replace(".", ""));

		WebElement totalbalancePro3 = this.element(total3);
		String saldoTotalDetodosProductos = totalbalancePro3.getText().trim();

		double totalBalancedetodosPro = Double
				.parseDouble(saldoTotalDetodosProductos.replace("$", "").replace(",", "").replace(".", ""));

		WebElement totalbalancePro4 = this.element(total4);
		String saldoTotalDetodosProductosFi = totalbalancePro4.getText().trim();

		double totalBalancedetodosProFi = Double
				.parseDouble(saldoTotalDetodosProductosFi.replace("$", "").replace(",", "").replace(".", ""));

		Reporter.reportEvent(Reporter.MIC_INFO, "Resumen de productos: ");

		Reporter.reportEvent(Reporter.MIC_INFO, "Resumen Saldo Total Ahorro e inversiones:");

		if (totalBalancePro == totalBalancedetodosPro) {
			Reporter.reportEvent(Reporter.MIC_PASS,
					"El saldo total coincide con el total resumido. " + saldoTotalDetodosProductos);
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "El saldo total " + saldoTotalProductos
					+ "No coincide con el total resumido. " + saldoTotalDetodosProductos);
		}

		Reporter.write("Resumen Saldo Total Financiación:");
		if (totalBalanceProFi == totalBalancedetodosProFi) {
			Reporter.reportEvent(Reporter.MIC_PASS,
					"El saldo total coincide con el total resumido. " + saldoTotalDetodosProductosFi);
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "El saldo total No " + saldoTotalProductosFi
					+ " coincide con el total resumido. " + saldoTotalDetodosProductosFi);
		}

	}

//=======================================================================================================================================

	public String formatearNumero(String numero) throws ParseException {
		try {
			// Elimina caracteres no numéricos y espacios antes de intentar convertir
			String numeroLimpio = numero.replaceAll("[^0-9.,]", "");

			// Reemplaza todas las comas por puntos para manejar el formato correcto
			numeroLimpio = numeroLimpio.replace(',', '.');

			// Verifica si el número ya tiene un formato correcto
			if (numeroLimpio.matches("\\d+\\.\\d+")) {
				return numeroLimpio;
			}

			// Parsea el número limpio
			double parsedNumber = Double.parseDouble(numeroLimpio);

			// Formatea el número y devuelve el resultado
			return String.format(Locale.US, "%.2f", parsedNumber);
		} catch (NumberFormatException e) {
			// Maneja la excepción si el número no se puede convertir
			e.printStackTrace();
			throw new ParseException("Error al formatear el número", 0);
		}
	}

//=======================================================================================================================================

	public String[] TabelMovi() throws ParseException {
		// Encuentra el elemento de la tabla por su ID (asumiendo que la tabla tiene un
		// ID)
		WebElement tabla = this.findElement(table);

		// Encuentra todas las filas en la tabla
		java.util.List<WebElement> filas = tabla.findElements(By.xpath("//tr[@class='CeldaGrilla']"));

		// Crea un array para almacenar los datos extraídos
		String[] movi = new String[filas.size()];

		// Recorre cada fila y extrae los datos requeridos
		for (int i = 0; i < filas.size(); i++) {
			WebElement fila = filas.get(i);

			// Encuentra todas las celdas en la fila
			java.util.List<WebElement> celdas = fila.findElements(By.tagName("td"));

			// Extrae datos de cada celda
			String fechatb = celdas.get(0).getText();
			String documento = celdas.get(1).getText();
			String descripcion = celdas.get(2).getText();
			String transaccion = celdas.get(3).getText();
			String oficina = celdas.get(4).getText();
			String nitOriginador = celdas.get(5).getText();
			String valorCheque = celdas.get(6).getText();
			String valorTotal = celdas.get(7).getText();

			// Concatena los datos y almacénalos en el array
			movi[i] = fechatb + ", " + documento + ", " + descripcion + ", " + transaccion + ", " + oficina + ", "
					+ nitOriginador + ", "
					+ this.formatearNumero(
							valorCheque.replace("$ ", "").trim().replace(".", "").trim().replace(",", ".").trim())
					+ ", " + this.formatearNumero(
							valorTotal.replace("$ ", "").trim().replace(".", "").trim().replace(",", ".").trim());

		}

		return movi;
	}

// =======================================================================================================================================

	public String[] TabelMovi2() throws ParseException {
		// Encuentra el elemento de la tabla por su ID (asumiendo que la tabla tiene un
		// ID)
		WebElement tabla = this.findElement(table);

		// Encuentra todas las filas en la tabla
		java.util.List<WebElement> filas = tabla.findElements(By.xpath("//tr[@class='CeldaGrilla']"));

		// Crea un array para almacenar los datos extraídos
		String[] movi = new String[filas.size()];

		// Recorre cada fila y extrae los datos requeridos
		for (int i = 0; i < filas.size(); i++) {
			WebElement fila = filas.get(i);

			// Encuentra todas las celdas en la fila
			java.util.List<WebElement> celdas = fila.findElements(By.tagName("td"));

			// Extrae datos de cada celda
			String fechatb = celdas.get(0).getText();
			String movimiento = celdas.get(1).getText();
			String valorTx = celdas.get(2).getText();

			// Concatena los datos y almacénalos en el array
			movi[i] = fechatb + ", " + movimiento + ", " + this
					.formatearNumero(valorTx.replace("$ ", "").trim().replace(".", "").trim().replace(",", ".").trim());

		}

		return movi;
	}
//=======================================================================================================================================

	public String[] Consulta(String Descripcion, String Transaccion, String Oficina) throws Exception {
		String filePath = "C:\\temp\\Tipos TX pyme.xls";

		try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
			Workbook workbook = WorkbookFactory.create(fileInputStream);

			// Leer datos de la Hoja1
			String mtvo = obtenerValorDeHoja(workbook.getSheet("Hoja1"), Descripcion);
			// Leer datos de la Hoja2
			String tipo = obtenerValorDeHoja(workbook.getSheet("Hoja2"), Transaccion);
			// Leer datos de la Hoja3

			String ofre = obtenerValorDeHoja(workbook.getSheet("Hoja3"), Oficina);

			String[] strat = { mtvo, tipo, ofre };

			return strat;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//=======================================================================================================================================

	private static String obtenerValorDeHoja(Sheet hoja, String descripcion) {
		String resultado = "No_Hay_resultado";

		for (Row row : hoja) {
			Cell cell = row.getCell(0); // Suponemos que la descripción está en la primera columna (columna 0)
			if (cell != null && cell.getStringCellValue().equals(descripcion)) {
				Cell mtvoCell = row.getCell(1); // Suponemos que el MTVO está en la segunda columna (columna 1)
				if (mtvoCell != null) {
					resultado = mtvoCell.getStringCellValue();
				}
				break; // Salir del bucle si se encuentra la descripción
			}
		}

		return resultado;
	}

	// =======================================================================================================================================

	// =======================================================================================================================================
	public String[] RegistrosMovimientos() throws Exception {
		// Encuentra el elemento de la tabla por su ID (asumiendo que la tabla tiene un
		// ID)

		try {
			DXCUtil.wait(2);
			if (this.element(tbDestinoFina) != null) {
				Reporter.write("**** Se ha encontrado información en una tabla de datos");
			} else if (this.element(tipoDestinoFondosMismoNit) != null) {
				Reporter.write("**** Se ha encontrado información sobre mismo NIT");
			} else if (this.element(tipoDestinoFondosCta) != null) {
				Reporter.write("**** Se ha encontrado información sobre una transacción");
			} else if (this.element(tipoDestinoServicios) != null) {
				Reporter.write("**** Se ha encontrado información sobre un pago de servicio");
			}
		} catch (Exception e) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontro información de Tx esperada.");
			this.pageOrigen.terminarIteracion();
		}

		String[] movi = null;
		if (this.element(tbDestinoFina) != null) {
			WebElement tabla = this.findElement(tbDestinoFina);
			// Encuentra todas las filas en la tabla
			List<WebElement> filas = tabla.findElements(By.xpath("//tr[@class='CeldaGrilla']"));

			// Crea un array para almacenar los datos extraídos
			movi = new String[filas.size()];

			// Recorre cada fila y extrae los datos requeridos
			for (int i = 0; i < filas.size(); i++) {
				WebElement fila = filas.get(i);

				// Encuentra todas las celdas en la fila
				List<WebElement> celdas = fila.findElements(By.tagName("td"));
				// Extrae datos de cada celda
				String bancoDestino = celdas.get(0).getText();
				String tipoDestino = celdas.get(1).getText();
				String numeroDestino = celdas.get(2).getText();
				String titular = celdas.get(3).getText();
				String valor = celdas.get(4).getText();
				String Estado = celdas.get(5).getText();
				String movtivoRe = celdas.get(6).getText();

				// Concatena los datos y almacénalos en el array
				movi[i] = bancoDestino + "|" + tipoDestino + "|"
						+ valor.replace("$ ", "").trim().replace(",", ".").trim() + "|" + Estado + "|" + movtivoRe;

				if (movtivoRe != null || !movtivoRe.isEmpty()) {
					Reporter.reportEvent(Reporter.MIC_INFO, tipoDestino + " " + titular + " " + movtivoRe);
				}
			}
		} else {
			movi = new String[1];
			String estado = "Pago Exitoso";
			String destino = "";
			WebElement destinoEncontrado = null;

			try {
				destinoEncontrado = this.findElement(tipoDestinoFondosMismoNit);
			} catch (Exception e) {
				try {
					destinoEncontrado = this.findElement(tipoDestinoFondosCta);
				} catch (Exception e2) {
					destinoEncontrado = this.findElement(tipoDestinoServicios);
				}
			}

			if (destinoEncontrado != null) {
				destino = destinoEncontrado.getText();
				if (destino.equalsIgnoreCase("Factura de servicios")) {
					destino = "Pago de Servicios";
				}
			}

			movi[0] = destino + "|" + estado;
			if (destinoEncontrado != null) {
				Reporter.reportEvent(Reporter.MIC_INFO, "Tipo destino: " + destino);
			}

		}

		return movi;
	}

}
