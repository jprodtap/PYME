package dav.divisas;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.transversal.DatosDavivienda;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
import dxc.util.DXCUtil;

public class PageEnviarTransInternacional extends PageDivisas {

//=======================================================================================================================	

	public PageEnviarTransInternacional(BasePageWeb parentPage) {
		super(parentPage);
	}

//=======================================================================================================================	

	public By submit = By.xpath("//*[contains(text(), 'Siguiente')]");

	By CerrSesion = By.xpath("//*[@id='CerrarSesion']");
	By sesionEx = By.xpath("//b[contains(text(), 'Sesión no existe o ha expirado por inactividad.')]");

	By btnSiguiente = By.xpath("//button[@type='submit']");

	By cmpPopup = By.xpath("//div[@id='AlertaModal']//h4//strong");
	By btnPopup = By.xpath("//*[@id='btnmodal']");

	By option = By.tagName("option");

//=======================================================================================================================	

	By moduloCotización = By.xpath("//h3/strong[contains(text(), 'Cotización')]");

	By inputMoneda = By.id("Moneda");
	By inputMonto = By.id("Monto");
	By inputConcepto = By.id("ConceptoCambiario");
	By inputNumeral1 = By.id("Numerales_NumeralCambiario_0");
	By inputNumeral2 = By.id("Numerales_NumeralCambiario_1");
	By inputValorNumeral1 = By.id("Numerales_valor_0");
	By inputValorNumeral2 = By.id("Numerales_valor_1");
	By inputDescripcion = By.id("DescripcionOperacion");
	String eliminarNumeralCambiario = "//*[@id='Eliminar_NUM']";

//=======================================================================================================================	

	By inputNombreBene = By.id("NombreBeneficiario");
	By inputPaisDestino = By.id("PaisSeleccion");
	String predictPaisDestino = "//li[contains(text(),'TIPODES')]";
	By inputCiudadDestino = By.id("Ciudad");
	By inputDireccionBene = By.id("Direccion");
	By inputCuentaBene = By.id("IbanClabe");

	By inputInfoPago = By.id("TipoInformacion");
	By inputReferenciaPago = By.id("InformacionBeneficiario");

//=======================================================================================================================	
	By radioFrecuente = By.xpath("//input[@value='EF']");
	By radioNuevo = By.xpath("//input[@value='NE']");

	By buttonAgregarNumeral = By.id("Adicionar_0");
//=======================================================================================================================	

	String locRadiobutton = "//tbody/tr[td[contains(text(), 'NUMCUENTA')] and td[contains(text(), 'TIPOCUENTA')]]/th/input[@type='radio']";
	String locSaldodisponible = "//tbody/tr[td[contains(text(), 'NUMCUENTA')] and td[contains(text(), 'TIPOCUENTA')]]/td[3]";
	String msg;

//=======================================================================================================================	

	String predictNumeroCodigo = "//li[contains(text(), 'numeroCodigo')]";
	String predictNumeroCodigoInter = null;

//=======================================================================================================================	
	By inputTipoCodigo = By.id("TipoCodigo");
	By inputNumeroCodigo = By.id("NumeroCodigo");

	By inputBancodelbeneficiario = By.id("banco");

	By radioSiIntermediario = By.id("TieneBancoIntermediario");

	By inputTipoCodigoInter = By.id("TipoCodigoIntermediario");
	By inputNumeroCodigoInter = By.id("NumeroCodigoIntermediario");

//=======================================================================================================================	

	By campconcepto = By.xpath("//*[@id='FormPaso5']/div[1]/div[2]/div[1]/div[2]");
	By camnumeralCambiario1 = By.xpath("//*[@id='FormPaso5']/div[1]/div[2]/div[2]/div[2]");
	By camdescripcion = By.xpath("//*[@id='FormPaso5']/div[1]/div[2]/div[3]/div[2]");
	By camcuenta = By.xpath("//*[@id='FormPaso5']/div[1]/div[2]/div[4]/div[2]");
	By camtipoCambio = By.xpath("//*[@id='FormPaso5']/div[2]/div[2]/div[3]/div[2]");
	By cammontoUSD = By.xpath("//*[@id='FormPaso5']/div[2]/div[2]/div[4]/div[2]");
	By camtasaCambio = By.xpath("//*[@id='FormPaso5']/div[2]/div[2]/div[5]/div[2]");
	By camvalorOperacion = By.xpath("//*[@id='FormPaso5']/div[2]/div[2]/div[6]/div[2]");
	By camcostoServicio = By.xpath("//*[@id='FormPaso5']/div[2]/div[2]/div[7]/div[2]");
	By camIva = By.xpath("//*[@id='FormPaso5']/div[2]/div[2]/div[8]/div[2]");
	By camValorTotal = By.xpath("//*[@id='FormPaso5']/div[2]/div[2]/div[9]/div[2]");

	By camnombreBeneficiario = By.xpath("//*[@id='FormPaso5']/div[3]/div[2]/div[1]/div[2]");
	By campaisBeneficiario = By.xpath("//*[@id='FormPaso5']/div[3]/div[2]/div[2]/div[2]");
	By camciudadBeneficiario = By.xpath("//*[@id='FormPaso5']/div[3]/div[2]/div[3]/div[2]");
	By camdireccionBeneficiario = By.xpath("//*[@id='FormPaso5']/div[3]/div[2]/div[4]/div[2]");
	By camcuentaBeneficiario = By.xpath("//*[@id='FormPaso5']/div[3]/div[2]/div[5]/div[2]");
	By caminfoBeneficiario = By.xpath("//*[@id='FormPaso5']/div[3]/div[2]/div[6]/div[2]");
	By camtipoCodigo = By.xpath("//*[@id='FormPaso5']/div[4]/div[2]/div[1]/div[2]");
	By camnumeroCodigo = By.xpath("//*[@id='FormPaso5']/div[4]/div[2]/div[2]/div[2]");
	By cambancoBeneficiario = By.xpath("//*[@id='FormPaso5']/div[4]/div[2]/div[3]/div[2]");

	By cambancoIntermediario = By.xpath("//*[@id='FormPaso5']/div[4]/div[2]/div[4]/div[2]");
	By camtipoCodigoIntermediario = By.xpath("//*[@id='FormPaso5']/div[4]/div[2]/div[5]/div[2]");
	By camnumeroCodigoIntermediario = By.xpath("//*[@id='FormPaso5']/div[4]/div[2]/div[6]/div[2]");

	By tablabeneficiariofrecuente = By.xpath("//div[@id='beneficiariofrecuente']//table");

	String xPathSelecBeneficiario = "//td[contains(text(),'PAIS')]//preceding-sibling::td[contains(text(),'CUENTABENE')]//preceding-sibling::td[contains(text(),'NOM')]//preceding-sibling::td[contains(text(),'FECHA')]//preceding-sibling::td//input";
//=======================================================================================================================	

	By sesionInactivacomuníquese = By.xpath("/html/body/div/div[2]/p/strong[2]"); // Si el error continúa comuniquese
																					// con nuestras líneas de atención
																					// personalizada.

	By focusInfo = By.xpath("//strong[contains(text(), 'Información')]");
	By popupConfirmacion = By.id("mensaje");
	By submitPopup = By.id("botonModal");
//=======================================================================================================================	
	String saldoTotalInicial = null;
	String saldoDispoInicial = null;
	String saldoTotalFinal = null;
	String saldoDispoFinal = null;

	private static int contador = 1;

//===================================================================================================================================

	/**
	 * Seleciona la cuenta origen Inicial de la tx si es cuenta Ahorros o Corriente.
	 * 
	 * @param servicio
	 * @param tipoIdEm
	 * @param nitEmpre
	 * @param tipoCuenta
	 * @param numeroCuenta
	 * @return vacio si es exitosa, si retorna un mensaje si es fallido
	 * @throws Exception
	 */
	public String seleccionarCuenta(String servicio, String tipoIdEm, String nitEmpre, String tipoCuenta,
			String numeroCuenta) throws Exception {

		String tipoProdUpper = tipoCuenta;
		String tipoProd = " "; // VALOR POR DEFECTO

		if (tipoProdUpper.contains("AHORROS") || tipoProdUpper.contains("ahorros"))
			tipoProd = "AHORROS";
		else if (tipoProdUpper.contains("CORRIENTE") || tipoProdUpper.contains("corriente")) // CRÉDIPLUS
			tipoProd = "CORRIENTE";

		WebElement radiobutton = null;

		contador = 0;
		do {
			contador++;

			DXCUtil.wait(1);

			msg = this.closeActiveIntAlert();
			if (isValid(msg) && !msg.contains("Para enviar o recibir transferencias"))
				return msg;

			DXCUtil.wait(1);

			radiobutton = this.element(this.locRadiobutton.replace("NUMCUENTA", DXCUtil.right(numeroCuenta, 4))
					.replace("TIPOCUENTA", tipoProd));

		} while (isElementPresentAndNotInteractable(radiobutton) && contador <= 30);

		// No se encuentra el registro, no selecciona y le da click al boton
		// siguiente.... prueba por mal

		if (radiobutton == null) {
			Evidence.save("No se encuentra el registro " + tipoProd + " - " + numeroCuenta);
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se encunetra el registro " + tipoProd + " - " + numeroCuenta);
			DXCUtil.wait(2);

			try {
				this.click(By.xpath("//button[@type='submit']"));
			} catch (Exception e) {
				return "No se encontro el elemento Boton siguiente";
			}

			this.ErrorSesionExpirada();

			String msg = this.closeActiveIntAlertConfirma();

			if (msg != null) {
				this.getDriver().switchTo().defaultContent();
				return msg;
			}

		} else {

			if (!DatosDavivienda.IS_RISKMOTOR)
				this.validacionSaldosStratus(servicio, tipoIdEm, nitEmpre, tipoProd, numeroCuenta, true);

			DXCUtil.wait(5);

			this.click(radiobutton);

			Reporter.reportEvent(Reporter.MIC_PASS, "Pantalla Origen");
		}

		this.getJse().executeScript("document.body.style.zoom ='90%';");

		Evidence.save("Cuenta seleccionada, ventana origen");

		this.getJse().executeScript("document.body.style.zoom ='100%';");

		DXCUtil.wait(1);
		this.click(btnSiguiente);

		this.ErrorSesionExpirada();

		return "";
	}

//===================================================================================================================================

	/**
	 * Ingresa los datos de la pantalla de valores dependiendo el tipo de moneda
	 * 
	 * @param tipoMoneda
	 * @param monto
	 * @param concepto
	 * @param numeral1
	 * @param numeral2
	 * @param valorNumeral1
	 * @param valorNumeral2
	 * @param descripcion
	 * @param tipoEnvio
	 * @return
	 * @throws Exception
	 */
	public String ingresarValores(String tipoMoneda, String monto, String concepto, String numeral1, String numeral2,
			String valorNumeral1, String valorNumeral2, String descripcion, String tipoEnvio) throws Exception {

		String msg = null;

		if (!isValid(tipoMoneda) || !isValid(monto) || !isValid(concepto) || !isValid(numeral1) || !isValid(numeral2)
				|| !isValid(valorNumeral1) || !isValid(valorNumeral2) || !isValid(descripcion) || !isValid(tipoEnvio)) {

			Reporter.reportEvent(Reporter.MIC_WARNING, "Algunos datos vacios en ventana Valores");
		}

		contador = 0;
		do {
			contador++;
			DXCUtil.wait(1);
			if (contador >= 30) {
				this.getDriver().switchTo().defaultContent();
				Evidence.save("Pantalla - Valores");
				return "Error No se cargo la pantalla de Valores";
			}

		} while (!isElementInteractable(inputMoneda));

		Evidence.save("Pantalla - Valores");

		// No se encuentra tipo de moneda.... prueba por mal
		try {

			if (this.element(inputMoneda).findElements(option).size() > 0) {

				msg = this.selectListItem(inputMoneda, tipoMoneda);

				if (isValid(msg) || msg.equals("Valor no en contrado")) {

					if (msg == null || msg.trim().isEmpty()) {
						msg = "No se encuentra el tipo de moneda";
					}

					Evidence.save(msg);
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}

			} else {

				return "El campo tipo moneda no tiene opciones.";
			}

		} catch (Exception e) {

			this.getDriver().switchTo().defaultContent();
			Evidence.save("No aparecen opciones en la lista desplegable");
			return "No aparecen opciones en la lista desplegable";

		}

		this.write(inputMonto, monto);
//		this.element(inputMonto).clear();
//		this.element(inputMonto).sendKeys(monto);

		DXCUtil.wait(4);

		// No se encuentra concepto.... prueba por mal
		try {

			if (this.element(inputConcepto).findElements(option).size() > 0) {
				msg = selectListItem(inputConcepto, concepto);

				if (isValid(msg) || msg.equals("Valor no en contrado")) {

					if (msg == null || msg.trim().isEmpty()) {
						msg = "No se encuentra el tipo de concepto";
					}

					Evidence.save(msg);
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}

			} else {

				return "El campo concepto no tiene opciones.";
			}

		} catch (Exception e) {

			this.getDriver().switchTo().defaultContent();
			Evidence.save("No aparecen opciones en la lista desplegable");
			return "No aparecen opciones en la lista desplegable";
		}

		if (concepto.contains("1 -") || concepto.contains("3 -")) {

			contador = 0;
			do {
				contador++;

				DXCUtil.wait(1);

				if (contador >= 30) {

					this.getDriver().switchTo().defaultContent();
					Evidence.save("No carga numeral1 completo");
					return "Error en la carga de las listas demplegables";
				}

			} while (this.element(inputNumeral1) == null);

			try {

				if (this.element(inputNumeral1).findElements(option).size() > 0) {

					DXCUtil.wait(1);

					msg = selectListItem(inputNumeral1, numeral1);

					if (isValid(msg) || msg.equals("Valor no en contrado")) {

						if (msg == null || msg.trim().isEmpty()) {
							msg = "No se encuentra el numeral en la lista";
						}

						Evidence.save(msg);
						Reporter.reportEvent(Reporter.MIC_FAIL, msg);
					}

				} else {
					Reporter.reportEvent(Reporter.MIC_FAIL, "El campo Numeral no tiene opciones.");
					return "El campo Numeral1 no tiene opciones.";
				}

			} catch (Exception e) {

				this.getDriver().switchTo().defaultContent();

				Evidence.save("No aparecen opciones en la lista desplegable");
				return "No aparecen opciones en la lista desplegable";
			}

		} else if (concepto.contains("4 -") || concepto.contains("5 -")) {

			contador = 0;
			do {
				contador++;

				DXCUtil.wait(1);

				if (contador >= 30) {

					this.getDriver().switchTo().defaultContent();
					Evidence.save("No carga numeral1 completo");
				}

			} while (this.element(inputNumeral1) == null);

			try {

				if (this.element(inputNumeral1).findElements(option).size() > 0) {

					msg = selectListItem(inputNumeral1, numeral1);

					if (isValid(msg) || msg.equals("Valor no en contrado")) {

						if (msg == null || msg.trim().isEmpty()) {
							msg = "No se encuentra el numeral en la lista";
						}

						Evidence.save(msg);
						Reporter.reportEvent(Reporter.MIC_FAIL, msg);
					}

				} else {
					Reporter.reportEvent(Reporter.MIC_FAIL, "El campo Numeral1 no tiene opciones.");
					return "El campo Numeral1 no tiene opciones.";
				}
			} catch (Exception e) {

				this.getDriver().switchTo().defaultContent();
				Evidence.save("No aparecen opciones en la lista desplegable");
				return "No aparecen las opciones en la lista desplegable";

			}

			if (isElementInteractable(inputDescripcion)) {

//				this.element(inputDescripcion).sendKeys(descripcion);

				this.write(inputDescripcion, descripcion);

			} else {

				Reporter.reportEvent(Reporter.MIC_INFO, "El campo descripcion no se presenta en la pantalla.");
			}
		} else {

			return "Concepto Transferencia no encontrado:" + concepto;
		}

		if (!concepto.contains("4 -")) {

			DXCUtil.wait(2);

			boolean salir = false;
			WebElement btnAgregarNumeralCambiario = null;

			if (!valorNumeral1.equals("")) {
				contador = 0;

				do {
					contador++;
					DXCUtil.wait(2);
					btnAgregarNumeralCambiario = this.element(buttonAgregarNumeral);

					if (isElementInteractable(btnAgregarNumeralCambiario)) {
						salir = true;
					}
					if (contador >= 30) {
						Reporter.reportEvent(Reporter.MIC_FAIL, "No se visualiza el boton 'Agregar Numeral Cambiario");
						salir = true;
					}

				} while (this.element(btnAgregarNumeralCambiario) == null && salir == false);

				if (this.element(buttonAgregarNumeral) != null) {
					this.click(buttonAgregarNumeral);
				}

				contador = 0;
				WebElement inputNumeral = null;
				do {
					contador++;
					DXCUtil.wait(2);
					inputNumeral = this.element(inputNumeral2);
					if (isElementInteractable(inputNumeral)) {
						salir = true;
					}
					if (contador >= 30) {
						Reporter.reportEvent(Reporter.MIC_FAIL, "No se visualiza el boton 'Agregar Numeral Cambiario");
						Evidence.save("No aparecen opciones en la lista desplegable");
						this.click(submit);
						salir = true;
						DXCUtil.wait(1);
					}

				} while (this.element(inputNumeral) == null && salir == false);

				if (!numeral2.equals("")) {
					try {

						if (this.element(inputNumeral2).findElements(option).size() > 0) {
							msg = selectListItem(inputNumeral2, numeral2);

							if (isValid(msg) || msg.equals("Valor no en contrado")) {

								if (msg == null || msg.trim().isEmpty()) {
									msg = "No se encuentra el numeral en la lista";
								}

								Evidence.save(msg);
								Reporter.reportEvent(Reporter.MIC_FAIL, msg);
							}

						} else {
							Reporter.reportEvent(Reporter.MIC_FAIL, "El campo Numeral no tiene opciones.");

							return "El campo Numeral2 no tiene opciones.";
						}

					} catch (Exception e) {

						this.getDriver().switchTo().defaultContent();
						Evidence.save("No aparecen opciones en la lista desplegable");
						return "No aparecen opciones en la lista desplegable";
					}
				}

				try {
					this.write(inputValorNumeral1, valorNumeral1);
//					this.element(inputValorNumeral1).sendKeys(valorNumeral1);
				} catch (Exception e) {
					this.getDriver().switchTo().defaultContent();
					Evidence.save("No aparecen el campo valo");
					return "No aparecen el campo valo";
				}
			}

			if (!numeral2.equals("")) {
				contador = 0;
				WebElement inputNumeral = null;
				do {
					contador++;
					DXCUtil.wait(2);
					inputNumeral = this.element(inputNumeral2);

					if (this.element(inputNumeral).isDisplayed()) {
						salir = true;
					}

					if (contador >= 30) {
						Reporter.reportEvent(Reporter.MIC_FAIL, "No se visualiza el boton 'Agregar Numeral Cambiario");
						Evidence.save("No aparecen opciones en la lista desplegable");
						this.click(submit);
						salir = true;
						DXCUtil.wait(1);
					}

				} while (this.element(inputNumeral) == null && salir == false);

				try {

					if (this.element(inputNumeral2).findElements(option).size() > 0) {
						msg = selectListItem(inputNumeral2, numeral2);

						if (isValid(msg) || msg.equals("Valor no en contrado")) {

							if (msg == null || msg.trim().isEmpty()) {
								msg = "No se encuentra el numeral en la lista";
							}

							Evidence.save(msg);
							Reporter.reportEvent(Reporter.MIC_FAIL, msg);
						}

					} else {
						Reporter.reportEvent(Reporter.MIC_FAIL, "El campo Numeral no tiene opciones.");

						return "El campo Numeral2 no tiene opciones.";
					}
				} catch (Exception e) {

					this.getDriver().switchTo().defaultContent();
					Evidence.save("No aparecen opciones en la lista desplegable");
					return "No aparecen opciones en la lista desplegable";
				}

				try {
					this.write(inputValorNumeral2, valorNumeral2);
//					this.element(inputValorNumeral2).sendKeys(valorNumeral2);
				} catch (Exception e) {
					this.getDriver().switchTo().defaultContent();
					Evidence.save("No aparecen el campo valor");
					return "No aparecen el campo valo";
				}
			} else {
				if (concepto.contains("5 -"))
					Reporter.reportEvent(Reporter.MIC_INFO,
							"concepto de la transferencia 5 - Servicios, transferencias y otros conceptos debe ser diligenciado numeral cambiario 1 y 2");
			}

		}

		if (tipoEnvio.contains("Envio Frecuente")) {
			this.click(radioFrecuente);
		} else if (tipoEnvio.contains("Nuevo Envio")) {
			this.click(radioNuevo);
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL,
					"Solo existe las opciones Envio Frecuente y Nuevo Envio para seleccionar");
		}

		Evidence.save("Valores ingresados en ventana 'Valores'");

		DXCUtil.wait(3);

		msg = null;
		contador = 0;
		do {
			DXCUtil.wait(1);
			contador++;
		} while (this.element(popupConfirmacion) == null && this.element(submitPopup) == null && contador < 7);

		if (this.element(popupConfirmacion) != null) {

			Evidence.save("popUp en valores");
			String mensajePopUP = this.findElement(popupConfirmacion).getText();

			if (mensajePopUP != null && !mensajePopUP.equals("")) {
				this.getDriver().switchTo().defaultContent();
				return mensajePopUP;
			}
		}

		msg = null;

		msg = this.closeActiveIntAlertConfirma();
		if (msg != null) {
			this.getDriver().switchTo().defaultContent();
			return msg;

		}
		this.click(submit);

		msg = this.closeActiveIntAlertConfirma();
		if (msg != null) {
			this.getDriver().switchTo().defaultContent();
			return msg;

		}

		return "";
	}

//===================================================================================================================================

	/**
	 * Datos registro Beneficiario
	 * 
	 * @param nombreBene
	 * @param paisDestino
	 * @param ciudadDestino
	 * @param direccionBene
	 * @param cuentaBene
	 * @param infoPago
	 * @param referenciaPago
	 * @throws Exception
	 */
	public String ingresarBeneficiario(String nombreBene, String paisDestino, String ciudadDestino,
			String direccionBene, String cuentaBene, String infoPago, String referenciaPago, String tipoEnvio)
			throws Exception {

		WebElement predicPaisDes = null;

//		if (tipoEnvio.equals("Nuevo Envio")) {
//			predicPaisDes = this.element(this.predictPaisDestino.replace("TIPODES", paisDestino.toUpperCase()));
//
//		}

		if (tipoEnvio.equals("Nuevo Envio")) {

			if (nombreBene.isEmpty() || paisDestino.isEmpty() || ciudadDestino.isEmpty() || direccionBene.isEmpty()
					|| cuentaBene.isEmpty() || infoPago.isEmpty() || referenciaPago.isEmpty()) {

				Reporter.reportEvent(Reporter.MIC_FAIL, "Algunos datos vacios en ventana Destino 1ra parte");
			}

		} else {

			if (!isValid(infoPago) || !isValid(referenciaPago)) {

				Reporter.reportEvent(Reporter.MIC_FAIL, "Algunos datos vacios en ventana Destino 1ra parte");
			}

		}

		contador = 0;
		do {
			contador++;
			DXCUtil.wait(1);
			if (contador >= 30) {
				this.getDriver().switchTo().defaultContent();
				return "TimeOut no se encontro el campo : Nómbre del beneficiario";

			}
		} while (!isElementInteractable(inputNombreBene));

		Evidence.save("Datos - Beneficiario");

		String msg;

		if (tipoEnvio.equals("Nuevo Envio")) {

			this.write(inputNombreBene, nombreBene);
//			this.element(inputNombreBene).sendKeys(nombreBene);

			DXCUtil.wait(1);

			this.write(inputPaisDestino, paisDestino);
//			this.element(inputPaisDestino).sendKeys(paisDestino);

			contador = 0;
			do {
				contador++;
				DXCUtil.wait(1);
				predicPaisDes = this.element(this.predictPaisDestino.replace("TIPODES", paisDestino.toUpperCase()));
			} while (predicPaisDes == null);

			DXCUtil.wait(1);

			try {

				this.click(this.element(this.predictPaisDestino.replace("TIPODES", paisDestino.toUpperCase())));

			} catch (Exception e) {

				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra el pais que coincida con la busqueda");
			}
		}

		DXCUtil.wait(1);
		if (tipoEnvio.equals("Nuevo Envio")) {

			this.write(inputCiudadDestino, ciudadDestino);
			this.write(inputDireccionBene, direccionBene);
			DXCUtil.wait(1);
			this.write(inputCuentaBene, cuentaBene);
			DXCUtil.wait(1);
//			this.element(inputCiudadDestino).sendKeys(ciudadDestino);
//			this.element(inputDireccionBene).sendKeys(direccionBene);
//			this.element(inputCuentaBene).sendKeys(cuentaBene);

		}

		this.element(inputInfoPago).sendKeys(infoPago);
		DXCUtil.wait(1);
		this.write(inputReferenciaPago, referenciaPago);

//		this.element(inputReferenciaPago).sendKeys(referenciaPago);

		Evidence.save("InfoBeneficiario");

		msg = null;

		try {

			msg = this.closeActiveIntAlertConfirma();

		} catch (Exception e) {

			Evidence.save("Paso correcto a ventana Destino 2da parte");
		}

		if (msg != null) {
			this.getDriver().switchTo().defaultContent();
			return msg;

		}

		this.click(submit);

		this.ErrorSesionExpirada();

		msg = this.closeActiveIntAlertConfirma();

		if (msg != null) {
			this.getDriver().switchTo().defaultContent();
			return msg;

		}
		return null;
	}

//===================================================================================================================================

	/**
	 * SeleccionarBeneficiario
	 * 
	 * @param nombreBene
	 * @param paisDestino
	 * @param cuentaBene
	 * @param FechaBene
	 * @throws Exception
	 */
	public String SeleccionarBeneficiario(String nombreBene, String paisDestino, String cuentaBene, String FechaBene)
			throws Exception {
		String msg = null;

		if (!isValid(nombreBene) || !isValid(paisDestino) || !isValid(cuentaBene) || !isValid(FechaBene)) {

			Reporter.reportEvent(Reporter.MIC_FAIL, "Algunos datos vacios en ventana Destino");
		}

		contador = 0;
		do {
			contador++;
			DXCUtil.wait(1);
			if (contador >= 30) {
				this.getDriver().switchTo().defaultContent();
				Evidence.save("Seleccione un envío frcuente");
				return "TimeOut no se encontro la tabla : Seleccione un envío frcuente";
			}

		} while (this.element(tablabeneficiariofrecuente) == null);

		Evidence.save("Seleccione-InfoBeneficiario");

		WebElement objRadioButton = null;
		String xPathInput = xPathSelecBeneficiario.replace("PAIS", paisDestino)
				.replace("CUENTABENE", DXCUtil.right(cuentaBene, 4)).replace("NOM", nombreBene)
				.replace("FECHA", FechaBene);

		List<WebElement> objRadioButtonLis = this.findElements(By.xpath(xPathInput));

		if (objRadioButtonLis.size() > 1) {

			Reporter.reportEvent(Reporter.MIC_FAIL,
					"Hay mas de un elemento de los datos del beneficiario: asegurece de agregar la Fecha de envío frecuente");
		} else {

			objRadioButton = this.element(xPathInput);
		}

		if (objRadioButton == null) {

			Evidence.save("Error en ventana 'Destino 1ra parte'");
			msg = this.getActiveIntAlert();
			if (msg != null) {
				if (!msg.isEmpty())
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
			}

			if (isElementInteractable(btnSiguiente)) {

				this.click(btnSiguiente);

				DXCUtil.wait(2);

				msg = this.getActiveIntAlert();

				if (msg != null) {
					if (!msg.isEmpty())
						Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}
			}

			this.getDriver().switchTo().defaultContent();
			return msg;
		}

		// SI LLEGA A ESTE PUNTO ES PORQUE EXISTE EL PRODUCTO ORIGEN DE LOS FONDOS
		this.click(objRadioButton);

		try {

			msg = this.closeActiveIntAlertConfirma();

		} catch (Exception e) {

			Evidence.save("Paso correcto a ventana Destino");
		}

		if (msg != null) {
			this.getDriver().switchTo().defaultContent();
			return msg;
		}

		this.click(submit);

		this.ErrorSesionExpirada();

		msg = this.closeActiveIntAlertConfirma();

		if (msg != null) {

			this.getDriver().switchTo().defaultContent();

			return msg;

		}

		return null;
	}

//===================================================================================================================================

	/**
	 * Datos Beneficiario
	 * 
	 * @param nombreBene
	 * @param paisDestino
	 * @param ciudadDestino
	 * @param direccionBene
	 * @param cuentaBene
	 * @param infoPago
	 * @param referenciaPago
	 * @return
	 * @throws Exception
	 */
	public String ValidarBeneficiario(String nombreBene, String paisDestino, String ciudadDestino, String direccionBene,
			String cuentaBene, String infoPago, String referenciaPago) throws Exception {

		String msg = null;
		WebElement predicPaisDes = null;

		contador = 0;
		do {
			contador++;
			DXCUtil.wait(1);
			if (contador >= 30) {
				this.getDriver().switchTo().defaultContent();
				Evidence.save("No se encuentra el campo : Nómbre del beneficiario");
				return "TimeOut no se encontro el campo : Nómbre del beneficiario";
			}
		} while (this.element(inputNombreBene) == null);
		
		contador = 0;
		do {
			contador++;
			DXCUtil.wait(1);
			if (contador >= 30) {
				this.getDriver().switchTo().defaultContent();
				Evidence.save("No se encuentra el campo : País destino");
				return "TimeOut no se encontro el campo : País destino";
			}
		} while (this.element(inputPaisDestino) == null);

//		do {
//			DXCUtil.wait(1);
//			predicPaisDes = this.element(this.predictPaisDestino.replace("TIPODES", paisDestino.toUpperCase()));
//			contador++;
//			DXCUtil.wait(1);
//			if (contador >= 30) {
//				this.getDriver().switchTo().defaultContent();
//				Evidence.save("No se encuentra el campo : País destino");
//				return "TimeOut no se encontro el campo : País destino";
//			}
//
//		} while (predicPaisDes == null);

		this.element(inputInfoPago).sendKeys(infoPago);
		DXCUtil.wait(1);
		this.write(inputReferenciaPago, referenciaPago);

//		this.element(inputInfoPago).sendKeys(infoPago);
//		this.element(inputReferenciaPago).sendKeys(referenciaPago);

		Evidence.save("InfoBeneficiario");

		try {

			msg = this.closeActiveIntAlertConfirma();

		} catch (Exception e) {
			Evidence.save("Paso correcto a ventana Destino 2da parte");

		}

		if (msg != null) {
			this.getDriver().switchTo().defaultContent();
			return msg;
		}

		this.click(submit);

		this.ErrorSesionExpirada();

		msg = this.closeActiveIntAlertConfirma();

		if (msg != null) {

			this.getDriver().switchTo().defaultContent();

			return msg;

		}

		return null;
	}

//===================================================================================================================================

	/**
	 * Datos destinos
	 * 
	 * @param tipoCodigo
	 * @param numeroCodigo
	 * @param intermediario
	 * @param tipoCodigoInter
	 * @param numeroCodigoInter
	 * @return
	 * @throws Exception
	 */
	public String datosDestino(String tipoCodigo, String numeroCodigo, String intermediario, String tipoCodigoInter,
			String numeroCodigoInter) throws Exception {

		String msg;

		predictNumeroCodigo = predictNumeroCodigo.replace("numeroCodigo", numeroCodigo);

		if (intermediario.equals("SI")) {

			if (!isValid(tipoCodigo) || !isValid(numeroCodigo) || !isValid(intermediario) || !isValid(tipoCodigoInter)
					|| !isValid(numeroCodigoInter)) {

				Reporter.reportEvent(Reporter.MIC_FAIL, "Algunos datos vacios en ventana Destino 2da parte");
			}

			if (numeroCodigo.equals(numeroCodigoInter)) {
				predictNumeroCodigo = predictNumeroCodigo.replace("numeroCodigo", numeroCodigo);
				predictNumeroCodigoInter = predictNumeroCodigo.replace(numeroCodigo, numeroCodigoInter);
			} else {
				predictNumeroCodigo = predictNumeroCodigo.replace("numeroCodigo", numeroCodigo);
				predictNumeroCodigoInter = predictNumeroCodigo.replace(numeroCodigo, numeroCodigoInter);
			}
		}

		if (intermediario.equals("NO")) {
			if (!isValid(tipoCodigo) || !isValid(numeroCodigo)) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Algunos datos vacios en ventana Destino 2da parte");
			}
		}

		int contador = 0;

		do {
			contador++;
			DXCUtil.wait(1);
			if (contador >= 30) {
				if (this.element(sesionInactivacomuníquese) != null) {
					String alert = this.getText(sesionInactivacomuníquese);
					this.getDriver().switchTo().defaultContent();
					return alert;
				}
			}

		} while (this.element(inputTipoCodigo) == null);

		DXCUtil.wait(2);

		this.element(inputTipoCodigo).sendKeys(tipoCodigo);

		this.write(inputNumeroCodigo, numeroCodigo);

//
//		this.element(inputNumeroCodigo).sendKeys(numeroCodigo);

		DXCUtil.wait(3);

		try {

			this.click(this.element(predictNumeroCodigo));

		} catch (Exception e) {

			Evidence.save("No se encuentra el banco beneficiario");

			contador = 0;

			WebElement submintclic = null;

			do {
				DXCUtil.wait(1);
				contador++;
				submintclic = this.element(submit);
				if (contador >= 30) {
					this.getDriver().switchTo().defaultContent();
					Evidence.save("No se encuentro el boton_siguiente");
					return "No se encuentro el boton_siguiente";
				}

			} while (submintclic == null);

			this.click(submit);

			DXCUtil.wait(3);

			return "No se encuentra el banco beneficiario";

		}

		if (intermediario.equals("SI")) {

			this.click(radioSiIntermediario);

			contador = 0;

			do {
				DXCUtil.wait(1);

				if (contador >= 30) {

					return "No se encuentro el campo ngrese número de código";
				}

			} while (this.element(inputTipoCodigoInter) == null);

			this.element(inputTipoCodigoInter).sendKeys(tipoCodigoInter);

			this.write(inputNumeroCodigoInter, numeroCodigoInter);

//
//			this.element(inputNumeroCodigoInter).sendKeys(numeroCodigoInter);

			contador = 0;

			do {
				DXCUtil.wait(4);
				contador++;
				if (contador == 4) {
					Evidence.save("No se encuentra el banco beneficiario intermediario");
					Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra el banco beneficiario intermediario");
				}

			} while (this.element(predictNumeroCodigoInter) == null && contador < 4);

			try {
				this.click(this.element(predictNumeroCodigoInter));
			} catch (Exception e) {
				Evidence.save("No se encuentra el banco beneficiario intermediario");
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra el banco beneficiario intermediario");
			}

		}

		this.click(submit);

		Evidence.save("InfoBancoDestino");

		this.ErrorSesionExpirada();

		do {
			DXCUtil.wait(1);
			contador++;
			if (contador >= 30) {
				Evidence.save("No se encuentro el boton siguiente");
				this.getDriver().switchTo().defaultContent();
				return "No se encuentro el boton_siguiente";
			}

		} while (this.element(submit) == null);

		msg = null;

		try {

			msg = this.closeActiveIntAlertConfirma();

		} catch (Exception e) {

			Evidence.save("Paso correcto a ventana 'Cotizacion'");
		}

		if (msg != null) {
			this.getDriver().switchTo().defaultContent();
			return msg;

		}

		return null;
	}

//===================================================================================================================================

	/**
	 * Selecion destino
	 * 
	 * @param tipoCodigo
	 * @param numeroCodigo
	 * @param intermediario
	 * @param tipoCodigoInter
	 * @param numeroCodigoInter
	 * @throws Exception
	 */
	public String ValidaciondatosSelecDestino(String tipoCodigo, String numeroCodigo, String intermediario,
			String tipoCodigoInter, String numeroCodigoInter) throws Exception {

		String msg = null;

		predictNumeroCodigo = predictNumeroCodigo.replace("numeroCodigo", numeroCodigo);

		if (intermediario.equals("SI")) {

			if (!isValid(tipoCodigo) || !isValid(numeroCodigo) || !isValid(intermediario) || !isValid(tipoCodigoInter)
					|| !isValid(numeroCodigoInter)) {

				Reporter.reportEvent(Reporter.MIC_FAIL, "Algunos datos vacios en ventana Destino 2da parte");
			}

			if (numeroCodigo.equals(numeroCodigoInter)) {
				predictNumeroCodigo = predictNumeroCodigo.replace("numeroCodigo", numeroCodigo);
				predictNumeroCodigoInter = predictNumeroCodigo.replace(numeroCodigo, numeroCodigoInter);

			} else {
				predictNumeroCodigo = predictNumeroCodigo.replace("numeroCodigo", numeroCodigo);
				predictNumeroCodigoInter = predictNumeroCodigo.replace(numeroCodigo, numeroCodigoInter);
			}
		}

		if (intermediario.equals("NO")) {

			if (!isValid(tipoCodigo) || !isValid(numeroCodigo)) {

				Reporter.reportEvent(Reporter.MIC_FAIL, "Algunos datos vacios en ventana Destino 2da parte");
			}
		}

		contador = 0;
		do {
			contador++;
			DXCUtil.wait(1);
			if (contador >= 30) {
				if (this.element(sesionInactivacomuníquese) != null) {
					String alert = this.getText(sesionInactivacomuníquese);
					this.getDriver().switchTo().defaultContent();
					return alert;
				}
				this.getDriver().switchTo().defaultContent();
				return "No se presento el campo Tipo Codigo";
			}
		} while (this.element(inputTipoCodigo) == null);

		DXCUtil.wait(2);

		String bancobeneficiarodato = this.element(inputBancodelbeneficiario).getText();

		if (intermediario.equals("SI")) {

			contador = 0;
			do {
				DXCUtil.wait(1);
				if (contador >= 30) {
					Evidence.save("No se encuentra la opción  intermediario");
					this.getDriver().switchTo().defaultContent();
					return "No se encuentra la opción  Intermediario";
				}
			} while (this.element(radioSiIntermediario) == null);

			this.click(radioSiIntermediario);

			contador = 0;
			do {
				DXCUtil.wait(1);
				if (contador >= 30) {
					Evidence.save("No se encuentra el Tipo codigo intermediario");
					this.getDriver().switchTo().defaultContent();
					return "No se encuentra el codigo Intermediario";
				}

			} while (this.element(inputTipoCodigoInter) == null);

			this.element(inputTipoCodigoInter).sendKeys(tipoCodigoInter);

			DXCUtil.wait(1);

			this.write(inputNumeroCodigoInter, numeroCodigoInter);

//
//			this.element(inputNumeroCodigoInter).sendKeys(numeroCodigoInter);

			contador = 0;
			do {
				DXCUtil.wait(1);
				contador++;
				if (contador >= 30) {
					Evidence.save("No se encuentra el banco beneficiariointermediario");
					this.getDriver().switchTo().defaultContent();
					return "No se encuentra el Banco Beneficiario Intermediario";
				}
			} while (this.element(predictNumeroCodigoInter) == null);

			try {

				this.click(this.element(predictNumeroCodigoInter));

			} catch (Exception e) {

				Evidence.save("No se encuentra el banco beneficiario intermediario");
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra el banco beneficiario intermediario");
			}

		}

		Evidence.save("InfoBancoDestino");

		contador = 0;
		do {
			DXCUtil.wait(1);
			contador++;
			if (contador >= 30) {
				Evidence.save("No se encuentro el boton_siguiente");
				this.getDriver().switchTo().defaultContent();
				return "No se encuentro el boton_siguiente";
			}

		} while (this.element(submit) == null);

		this.click(submit);

		this.ErrorSesionExpirada();

		try {
			msg = this.closeActiveIntAlertConfirma();

		} catch (Exception e) {
			Evidence.save("Paso correcto a ventana 'Cotizacion'");
		}

		if (msg != null) {
			this.getDriver().switchTo().defaultContent();
			return msg;

		}

		return null;
	}

//=========================================================================================================================================

	/**
	 * Pantalla de cotizacion cuenta con 5 minutos para confirmar
	 * 
	 * @throws Exception
	 */
	public String cotizacion() throws Exception {
		contador = 0;
		do {
			DXCUtil.wait(1);
			contador++;
			if (contador >= 30) {
				Evidence.save("No se encuentra el Modulo: Cotizacion");
				this.getDriver().switchTo().defaultContent();
				return "No se encuentra el Modulo-Cotizacion";
//				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra el Modulo Cotizacion");
			}
		} while (this.element(moduloCotización) == null);

		DXCUtil.wait(4);

		this.focus(submit);
		Evidence.save("InfoCotizacion");
		try {
			this.click(submit);
		} catch (Exception e) {

			Evidence.save("Error en banco destino");
			this.getDriver().switchTo().defaultContent();
			return "Error en banco_destino";
		}

		this.ErrorSesionExpirada();
		contador = 0;
		do {
			DXCUtil.wait(2);
			if (contador >= 30) {
				Evidence.save("No se presento el Popup Mensaje 5 minutos");
				this.getDriver().switchTo().defaultContent();
				return "No se presento el Popup Mensaje";
			}
		} while (!isElementInteractable(popupConfirmacion) && !isElementInteractable(submitPopup));
		;

		this.focusInIframe(this.iframeIdDivisas, "//strong[contains(text(), 'Información')]");

		String mensajePopUP = this.findElement(popupConfirmacion).getText();

		if (mensajePopUP.contains("cuenta con 5 minutos para confirmar")) {

			this.click(submitPopup);

		} else {

			return mensajePopUP;
		}

		DXCUtil.wait(5);

		String msg = this.closeActiveIntAlert();

		if (msg != null) {

			return msg;
		}

		return null;
	}

//=========================================================================================================================================

	/**
	 * Pantalla de Confirmación
	 * 
	 * @param intermediario
	 * @return
	 * @throws Exception
	 */
	public String confirmacion(String intermediario) throws Exception {

		// Subtitle : Confirmacion
		contador = 0;
		do {
			DXCUtil.wait(1);
			contador++;

			if (contador >= 30) {

				Evidence.save("No se encuentra el modulo: Confirmar");
				this.getDriver().switchTo().defaultContent();
				return "No se encuentra el modulo: Confirmar";
			}

		} while (this.element(campconcepto) == null);

		this.getDriver().switchTo().defaultContent();

		this.switchToFrameDivisas();

		String concepto = this.element(campconcepto).getText();

		String numeralCambiario1 = this.element(camnumeralCambiario1).getText();

		String descripcion = this.element(camdescripcion).getText();

		if (this.element(camcuenta) != null) {

			String cuenta = this.element(camcuenta).getText();
		}

		// Subtitle : Detalles de Transacción
		String tipoCambio = null;
		String montoUSD = null;
		String tasaCambio = null;
		String valorOperacion = null;
		String costoServicio = null;
		String iva = null;
		String valorTotal = null;

		if (this.element(camtipoCambio) != null)
			tipoCambio = this.element(camtipoCambio).getText();

		if (this.element(cammontoUSD) != null)
			montoUSD = this.element(cammontoUSD).getText();

		if (this.element(camtasaCambio) != null)
			tasaCambio = this.element(camtasaCambio).getText();

		if (this.element(camvalorOperacion) != null)
			valorOperacion = this.element(camvalorOperacion).getText();

		if (this.element(camcostoServicio) != null)
			costoServicio = this.element(camcostoServicio).getText();

		if (this.element(camIva) != null)
			iva = this.element(camIva).getText();

		if (this.element(camtipoCambio) != null)
			valorTotal = this.element(camValorTotal).getText();

		// Adicionar los parámetros indicados por [nameParameters] a la hoja de datos
		// actual.
		// Si intenta adicionar un parámetro existente se ignora.
		SettingsRun.getTestData().addParametersNotExist("Tipo de cambio a USD", "Monto en USD", "Tasa de cambio",
				"Valor de la operación en pesos colombianos", "Costo del servicio", "IVA sobre costo del servicio",
				"Valor total a descontar de la cuenta", "Valor Neto a recibir", "Tipo de transferencia", "Fecha tx",
				"Hora tx", "Estado", "Nombre de Usuario", "Número Aprobación");

		if (!isValid(tipoCambio))
			// Almacena en el parámetro [nameParameter] del archivo de datos que se
			// encuentra en la ejecución actual,el String indicado [value].
			SettingsRun.getTestData().setParameter("Tipo de cambio a USD", tipoCambio);

		if (!isValid(montoUSD))
			SettingsRun.getTestData().setParameter("Monto en USD", montoUSD);

		if (!isValid(tasaCambio))
			SettingsRun.getTestData().setParameter("Tasa de cambio", tasaCambio);

		if (!isValid(valorOperacion))
			SettingsRun.getTestData().setParameter("Valor de la operación en pesos colombianos", valorOperacion);

		if (!isValid(costoServicio))
			SettingsRun.getTestData().setParameter("Costo del servicio", costoServicio);

		if (!isValid(iva))
			SettingsRun.getTestData().setParameter("IVA sobre costo del servicio", iva);

		if (!isValid(valorTotal))
			SettingsRun.getTestData().setParameter("Valor total a descontar de la cuenta", valorTotal);

		// Subtitle : Datos del Beneficiario

		String nombreBeneficiario = this.element(camnombreBeneficiario).getText();
		String paisBeneficiario = this.element(campaisBeneficiario).getText();
		String ciudadBeneficiario = this.element(camciudadBeneficiario).getText();
		String direccionBeneficiario = this.element(camdireccionBeneficiario).getText();
		String cuentaBeneficiario = this.element(camcuentaBeneficiario).getText();
		String infoBeneficiario = this.element(caminfoBeneficiario).getText();

		// Subtitle : Datos del Banco
		String tipoCodigo = this.element(camtipoCodigo).getText();
		String numeroCodigo = this.element(camnumeroCodigo).getText();
		String bancoBeneficiario = this.element(cambancoBeneficiario).getText();

		if (intermediario.equals("SI")) {
			String bancoIntermediario = this.element(cambancoIntermediario).getText();
			String tipoCodigoIntermediario = this.element(camtipoCodigoIntermediario).getText();
			String numeroCodigoIntermediario = this.element(camnumeroCodigoIntermediario).getText();
		}

		return null;
	}

//=========================================================================================================================================

	/**
	 * Realiza la validacion de los saldos Iniciales en Stratus comparando los saldo
	 * Totales del Portal
	 * 
	 * @param tipoCta
	 * @param numCta
	 * @param saldoOrigen
	 * @throws Exception
	 */

	public void validacionSaldosStratus(String servicio, String tipoDocumento, String numeroDoc, String tipoCta,
			String numCta, boolean saldoOrigen) throws Exception {

		if (DatosDavivienda.STRATUS != null) {

			String saldoDis = null;
			String saldoDisponible = null;

			if (saldoOrigen) {

				saldoDis = this.getText(this.element(locSaldodisponible.replace("NUMCUENTA", DXCUtil.right(numCta, 4))
						.replace("TIPOCUENTA", tipoCta)));

				saldoDisponible = saldoDis.replace("$ ", "").replace(".", "").replace(",", ".");

			}

			if (!DatosDavivienda.IS_RISKMOTOR) {

				if (tipoCta.contains("DIPLUS") || tipoCta.contains("diplus")) {

					tipoCta = "CREDITO";
					String[] datosCrediplus = DatosDavivienda.STRATUS.getCtaCobroCrediplus(numCta);

					tipoCta = datosCrediplus[0];
					numCta = datosCrediplus[1];
				}

				String[] saldosDisponibles = DatosDavivienda.STRATUS.consultarDatosPantallaSaldos(tipoDocumento,
						numeroDoc, tipoCta, numCta);
				DXCUtil.BonotesTecla("ALTTAB");
				if (saldoOrigen) {

					// Elimina el carácter '$' y espacios de ambas cadenas antes de formatear
					if (tipoCta.contains("ahorro") || tipoCta.contains("AH")) {
						this.saldoTotalInicial = saldosDisponibles[0].replace("$ ", "");
						this.saldoDispoInicial = saldosDisponibles[1].replace("$ ", "");
					}
					if (tipoCta.contains("corriente") || tipoCta.contains("CC")) {
						this.saldoTotalInicial = saldosDisponibles[6].replace("$ ", "");
						this.saldoDispoInicial = saldosDisponibles[0].replace("$ ", "");

					}

					Reporter.reportEvent(Reporter.MIC_INFO,
							"El saldo stratus Total Inicial: " + this.saldoTotalInicial.replace(".", ","));
					Reporter.reportEvent(Reporter.MIC_INFO, "El saldo stratus Disponible Efectivo Inicial: "
							+ this.saldoDispoInicial.replace(".", ","));

				} else if (!saldoOrigen) {

					// Elimina el carácter '$' y espacios de ambas cadenas antes de formatear
					if (tipoCta.contains("ahorro") || tipoCta.contains("AH")) {
						this.saldoTotalFinal = saldosDisponibles[0].replace("$ ", "");
						this.saldoDispoFinal = saldosDisponibles[1].replace("$ ", "");
					}

					if (tipoCta.contains("corriente") || tipoCta.contains("CC")) {
						this.saldoTotalFinal = saldosDisponibles[6].replace("$ ", "");
						this.saldoDispoFinal = saldosDisponibles[0].replace("$ ", "");
					}

					Reporter.reportEvent(Reporter.MIC_INFO,
							"El saldo Total stratus Final: " + this.saldoTotalFinal.replace(".", ","));
					Reporter.reportEvent(Reporter.MIC_INFO,
							"El saldo Disponible Efectivo stratus Final: " + this.saldoDispoFinal.replace(".", ","));

				}

				if (saldoOrigen) {
					if (this.saldoDispoInicial.contains(saldoDisponible)) {

						Reporter.reportEvent(Reporter.MIC_INFO,
								"El saldo total en stratus " + saldoDispoInicial.replace(".", ",")
										+ " corresponde al saldo disponible en el portal: " + saldoDis);
					} else {
						Reporter.reportEvent(Reporter.MIC_INFO, "El saldo total en stratus " + saldoDispoInicial
								+ " no corresponde al saldo disponible en el portal:" + saldoDis);
					}

					Reporter.write(" ");
				}
			}
		}
	}

//=========================================================================================================================================

	public String getSaldoTotalInicialOrigen() {
		return this.saldoTotalInicial;
	}

	public String getSaldoDispoInicialOrigen() {
		return this.saldoDispoInicial;
	}

	public String getSaldoTotalFinalOrigen() {
		return this.saldoTotalFinal;
	}

	public String getSaldoDispoFinalOrigen() {
		return this.saldoDispoFinal;
	}

}
