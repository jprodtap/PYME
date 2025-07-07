package dav.divisas;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.transversal.DatosDavivienda;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;

import dxc.util.DXCUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PageRecibirTransferenciasInternacionales extends PageDivisas {

	public PageRecibirTransferenciasInternacionales(BasePageWeb parentPage) {
		super(parentPage);
	}

// =======================================================================================================================

	// Locator Recibir del exterior Origen
	By ruta = By.id("lblMasterMigajasPan");
	By tituloTxIntern = By.xpath("//h3[contains(text(), 'Transferencias internacionales')]");

	By cmpPopup = By.xpath("//div[@id='AlertaModal']//h4//strong");
	By btnPopup = By.xpath("//*[@id='btnmodal']");
	By tituloModulo = By.xpath("//h3[text()='Recibir desde el exterior']");
	By titMoInformacion = By.xpath("//*[@id='Informacion']/div/h3");

// =======================================================================================================================

	By tbOrigen = By.xpath("//table[@class='table  table-responsive table-condensed table-hover table-bordered mt10']");

	String numRefTxIntxpath = "//tbody/tr[td[contains(text(), 'MONEDA_INTERNACIONAL')] and td[contains(text(), 'NUM_REF_RECI_INTERNACIONAL')]]/th/input[@type='radio']";
	String IntxpathOrdenante = "//tbody/tr[td[contains(text(), 'MONEDA_INTERNACIONAL')] and td[contains(text(), 'NUM_REF_RECI_INTERNACIONAL')]]/td[4]";
	String cuentaDesTxIntxpath = "//tbody/tr[td[contains(text(), 'NUM_PROD')] and td[contains(text(), 'TIPO_PROD')]]/th/input[@type='radio']";

	String locSaldodisponible = "//tbody/tr[td[contains(text(), 'NUMCUENTA')] and td[contains(text(), 'TIPOCUENTA')]]/td[3]";

	String xpathMonto = "//tbody/tr[td[contains(text(), 'MONEDA_INTERNACIONAL')] and td[contains(text(), 'NUM_REF_RECI_INTERNACIONAL')]]/td[3]";
	String xpathCamposCotiza = "//*[@id='FormPaso3']/div[1]/div[NUMERO_FILA]/div[2]";

	By seleConceptotxCambiario = By.xpath("//select[@id='ConceptoCambiario']");

	By seleNumCambiario = By.xpath("//select[@id='Numerales_NumeralCambiario_0']");
	By seleNumCambiario2 = By.xpath("//select[@id='Numerales_NumeralCambiario_1']");

	By buttonAgregarNumeral = By.id("Adicionar_0");
	By buttonEliminarNumeral1 = By.id("Eliminar_0");
	By buttonEliminarNumeral2 = By.id("Eliminar_1");

	By campValorIn = By.xpath("//input[@id='Numerales_valor_0']");
	By campValorIn2 = By.xpath("//input[@id='Numerales_valor_1']");

	By ButtonSigIn = By.xpath("//button[@class='dm-boton-proceso dm-btn-fondo-rojo']");

	By tituMoAbono = By.xpath("//strong[text()='Cuenta para abono']");
	By tituMoCotizar = By.xpath("//strong[text()='Cotizar la transferencia']");
	By tituMoConfirmacion = By.xpath("//strong[text()='Recibir desde el exterior']");

	By cmpMonto = By.xpath("//*[@id='FormPaso4']/div[7]/div[2]/label");
	By cmpTipodecambioaUSD = By.xpath("//*[@id='FormPaso4']/div[8]/div[2]/label");
	By cmpMontoenUSD = By.xpath("//*[@id='FormPaso4']/div[9]/div[2]/label");
	By cmpTasadecambio = By.xpath("//*[@id='FormPaso4']/div[10]/div[2]/label");
	By cmpVapesoscolombianos = By.xpath("//*[@id='FormPaso4']/div[11]/div[2]/label");
	By cmpCostoServicio = By.xpath("//*[@id='FormPaso4']/div[12]/div[2]/label");
	By cmpIVAservicio = By.xpath("//*[@id='FormPaso4']/div[13]/div[2]/label");
	By cmpValornetoarecibir = By.xpath("//*[@id='FormPaso4']/div[14]/div[2]/label/strong");

	By sesionEx = By.xpath("//b[contains(text(), 'Sesión no existe o ha expirado por inactividad.')]");

// =======================================================================================================================

	float valorNumeral, valorMonto;

	String saldoTotalInicial = null;
	String saldoDispoInicial = null;
	String saldoTotalFinal = null;
	String saldoDispoFinal = null;
	private static int contador = 1;

// =======================================================================================================================

	public String TxInternacionalesOrigen(String referencia, String moneda, String concepTx, String numCambiario1,
			String numCambiario2, String valorNumeral1, String valorNumeral2) throws Exception {
		
		String msg = null;

		Evidence.save("Divisas");
// -----------------------------------------------------------------------------------------------------------------------
		Evidence.save("Transferencias Int");

		this.closeActiveIntAlert();
		DXCUtil.wait(3);
		msg = this.seleccionarOrigenInter(referencia, moneda);

		if (isValid(msg)) {
			return msg;
		}

//--------------------Seleccione concepto de la transferencia:		

		WebElement lisConcepto = this.element(seleConceptotxCambiario);
		contador = 0;
		do {
			DXCUtil.wait(1);
			contador++;
			if (contador >= 30) {
				if (this.element(sesionEx) != null) {
					msg = this.element(sesionEx).getText();
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}
				return "TimeOut no se presento el campo Cambiario: Seleccione concepto de la transferencia";
			}
		} while (lisConcepto == null || !lisConcepto.isEnabled());

		if (isValid(concepTx)) {
			// Verificar si el elemento <select> tiene opciones
			if (lisConcepto.findElements(By.tagName("option")).size() > 0) {
				msg = this.selectListItem(seleConceptotxCambiario, concepTx);
				Evidence.save("Campo-Seleccione concepto de la transferencia");
				if (isValid(msg)) {
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
					this.click(ButtonSigIn);
					msg = this.closeActiveIntAlertConfirma();
					if (isValid(msg)) {
						return msg;
					}
				}

			} else {
				// el campo esta vacio da clic al boton siguiente
				Evidence.save("Campo-Seleccione concepto de la transferencia");
				this.click(ButtonSigIn);
				msg = this.closeActiveIntAlertConfirma();
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);

				return "El campo concepto cambiario de la transferencia no tiene opciones.";
			}

		} else {
			// el campo esta vacio da clic al boton siguiente
			Evidence.save("Campo-Seleccione concepto de la transferencia");
			this.click(ButtonSigIn);
			msg = this.closeActiveIntAlertConfirma();
			return msg;
		}

//--------------------Seleccione numeral cambiario:
		WebElement lisNumCambiario = null;

//--------------------Valida el concepto cambiario

		if (concepTx.contains("2") || concepTx.contains("3")) {

			Evidence.save("Origen");

//--------------------Seleccione numeral cambiario:		

			if (isValid(numCambiario1)) {

				try {
					contador = 0;
					// --------------------Seleccione numeral cambiario:
					do {
						DXCUtil.wait(1);
						lisNumCambiario = this.element(seleNumCambiario);
						contador++;
						if (contador > 30) {
							if (this.element(sesionEx) != null) {
								msg = this.element(sesionEx).getText();
								Reporter.reportEvent(Reporter.MIC_FAIL, msg);
							}
							return "TimeOut no se presento el campo numCambiario";
						}
					} while (!isElementInteractable(lisNumCambiario));

					msg = this.seleOptionGetAttribu(seleNumCambiario, numCambiario1);

				} catch (Exception e) {
					Evidence.save("No se presento el campo numCambiario");
					this.click(ButtonSigIn);
					Reporter.reportEvent(Reporter.MIC_FAIL, "No se presento el campo numCambiario");
					return "No se encontro la opción";
				}

				if (!isValid(msg)) {
					// el campo esta vacio da clic al boton siguiente
					Evidence.save("Campo-Seleccione numeral cambiario");
					this.click(ButtonSigIn);
					msg = this.closeActiveIntAlertConfirma();
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
					return "No se encontro la opción " + numCambiario1;
				}

			} else {
				// dar clic en siguiente el campo esta vacio da clic al boton siguiente
				Evidence.save("Campo-Seleccione numeral cambiario");
				this.click(ButtonSigIn);
				msg = this.closeActiveIntAlertConfirma();

				return msg;

			}

//------------------------- Campo Valor 1			

			if (isValid(valorNumeral1)) {
				contador = 0;
				do {
					DXCUtil.wait(1);
					contador++;
					if (contador >= 30) {
						if (this.element(sesionEx) != null) {
							msg = this.element(sesionEx).getText();
							Reporter.reportEvent(Reporter.MIC_FAIL, msg);
						}
						return "TimeOut no se presento el campo Valor";
					}
				} while (!isElementInteractable(campValorIn));

				if (isElementInteractable(campValorIn) && !valorNumeral1.isEmpty()) {

					this.write(campValorIn, valorNumeral1);

				}
			} else {
				// dar clic en siguiente el campo esta vacio da clic al boton siguiente
				Evidence.save("Campo-Valor 1");
				this.click(ButtonSigIn);
				msg = this.closeActiveIntAlertConfirma();

				return msg;
			}

//---------------------------------Seleccione numeral cambiario2:

			if (isValid(numCambiario2) || isValid(valorNumeral2)) {
				click(buttonAgregarNumeral);
			}

			if (isValid(numCambiario2)) {
				contador = 0;
				do {
					DXCUtil.wait(1);
					contador++;
					if (contador >= 30) {
						if (this.element(sesionEx) != null) {
							msg = this.element(sesionEx).getText();
							Reporter.reportEvent(Reporter.MIC_FAIL, msg);
						}
						return "TimeOut no se presento el campo numCambiario";
					}

				} while (!isElementInteractable(seleNumCambiario2));
//			} while (this.element(seleNumCambiario2) == null && !this.element(seleNumCambiario2).isEnabled());

				msg = this.seleOptionGetAttribu(seleNumCambiario2, numCambiario2);

				if (!isValid(msg)) {
					// dar clic a siguiente
					Evidence.save("Campo-Seleccione numeral cambiario 2");
					this.click(ButtonSigIn);
					msg = this.closeActiveIntAlertConfirma();
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
					return "No se encontro la opción " + numCambiario2;

				}

				contador = 0;
				do {
					DXCUtil.wait(1);
					contador++;
					if (contador >= 30) {
						if (this.element(sesionEx) != null) {
							msg = this.element(sesionEx).getText();
							Reporter.reportEvent(Reporter.MIC_FAIL, msg);
						}

						return "TimeOut no se presento los botones eliminar numeral cambiario";
					}
				} while (!isElementInteractable(buttonEliminarNumeral1));

				contador = 0;

				do {
					DXCUtil.wait(1);
					contador++;
					if (contador >= 30) {
						if (this.element(sesionEx) != null) {
							msg = this.element(sesionEx).getText();
							Reporter.reportEvent(Reporter.MIC_FAIL, msg);
						}

						return "TimeOut no se presento los botones eliminar numeral cambiario";
					}

				} while (!isElementInteractable(buttonEliminarNumeral2));

			} else {
				// dar clic siguiente el campo esta vacio da clic al boton siguiente
				Evidence.save("Campo-Seleccione numeral cambiario 2");
				this.click(ButtonSigIn);

				msg = this.closeActiveIntAlertConfirma();

				return msg;
			}

//------------------------- Campo Valor 2

			if (isValid(valorNumeral2)) {
				contador = 0;
				do {
					DXCUtil.wait(1);
					contador++;
					if (contador >= 30) {
						if (this.element(sesionEx) != null) {
							msg = this.element(sesionEx).getText();
							Reporter.reportEvent(Reporter.MIC_FAIL, msg);
						}
						return "TimeOut no se presento el campo Valor";
					}
				} while (!isElementInteractable(campValorIn2));

				if (isElementInteractable(campValorIn2) && !valorNumeral2.isEmpty()) {

					this.write(campValorIn2, valorNumeral2);
				}

			} else {
				// dar clic en siguiente el campo esta vacio da clic al boton siguiente
				Evidence.save("Campo-Valor 2");
				this.click(ButtonSigIn);
				msg = this.closeActiveIntAlertConfirma();

				return msg;
			}

			Evidence.save("Datos seleccionados Origen");

		} else if (concepTx.contains("5") || concepTx.contains("4")) {

			// --------------------Seleccione numeral cambiario:
			if (isValid(numCambiario1)) {
				contador = 0;
				do {
					DXCUtil.wait(1);
					lisNumCambiario = this.element(seleNumCambiario);
					contador++;
					if (this.element(lisNumCambiario) == null && contador == 20) {

						msg = this.selectListItem(seleConceptotxCambiario, concepTx);

						Evidence.save("Campo-Seleccione concepto de la transferencia");

						if (isValid(msg)) {

							Reporter.reportEvent(Reporter.MIC_FAIL, msg);
							this.click(ButtonSigIn);

							msg = this.closeActiveIntAlertConfirma();

							if (isValid(msg)) {
								return msg;
							}
						}
					}

					if (contador >= 30) {

						if (this.element(sesionEx) != null) {
							msg = this.element(sesionEx).getText();
							Reporter.reportEvent(Reporter.MIC_FAIL, msg);
						}

						return "TimeOut no se presento el campo numCambiario";
					}

				} while (!isElementInteractable(lisNumCambiario));

				msg = this.seleOptionGetAttribu(seleNumCambiario, numCambiario1);

				if (!isValid(msg)) {
					// el campo esta vacio da clic al boton siguiente
					Evidence.save("Campo-Seleccione numeral cambiario");
					this.click(ButtonSigIn);
					msg = this.closeActiveIntAlertConfirma();
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
					return "No se encontro la opción " + numCambiario1;
				}

			} else {
				// dar clic en siguiente el campo esta vacio da clic al boton siguiente
				Evidence.save("Campo-Seleccione numeral cambiario");
				this.click(ButtonSigIn);
				msg = this.closeActiveIntAlertConfirma();

				return msg;
			}

		} else {

			return "Concepto Transferencia no encontrado:" + concepTx;
		}

		contador = 0;

		do {
			DXCUtil.wait(1);
			contador++;
			if (contador >= 30) {
				if (this.element(sesionEx) != null) {
					msg = this.element(sesionEx).getText();
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}
				return "TimeOut no se presento el boton siguiente";
			}

		} while (!isElementInteractable(ButtonSigIn));

		this.click(ButtonSigIn);

		msg = this.closeActiveIntAlert();

		if (isValid(msg)) {
			return msg;
		}

		// Valida que la suma de los dos numerales, si es el caso, sea igual al de la
		// transaccion

		if (!isValid(valorNumeral1)) {
			valorNumeral1 = "0";
		}

		if (!isValid(valorNumeral2)) {
			valorNumeral2 = "0";
		}

		if (valorNumeral2.isEmpty()) {
			valorNumeral = Integer.parseInt(valorNumeral1);

		} else {
			valorNumeral = Integer.parseInt(valorNumeral1) + Integer.parseInt(valorNumeral2);
		}

		if (valorMonto == valorNumeral) {
			Reporter.reportEvent(Reporter.MIC_PASS,
					"La suma del valor de los numerales coninciden con el valor del monto");
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL,
					"La suma del valor de los numerales NO coninciden con el valor del monto");
		}

		return msg;
	}

// =======================================================================================================================

	public String seleccionarOrigenInter(String numRef, String moneda) throws Exception {

		contador = 0;

		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA PARA LA SELECCI�N DEL ORIGEN
			DXCUtil.wait(1);
			contador++;
			if (contador >= 30) {
				if (this.element(sesionEx) != null) {
					String msg = this.element(sesionEx).getText();
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}
				return "TimeOut No se presentó el subtitulo Recibir desde el exterior";
			}
		} while (this.element(tituloModulo) == null);
// -----------------------------------------------------------------------------------------------------------------------
//			Evidence.saveAllScreens("Recibir");
		Evidence.save("Recibir");
		WebElement objTablaOrigenes;
		contador = 0;
		do { // ESPERA MIENTRAS NO SE PRESENTA LA TABLA DE OR�GENES
			DXCUtil.wait(1);
			objTablaOrigenes = this.element(tbOrigen);
			contador++;
			if (contador >= 30) {
				if (this.element(sesionEx) != null) {
					String msg = this.element(sesionEx).getText();
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}
				return "TimeOut No se presentó la tabla del origen";

			}

		} while (this.element(objTablaOrigenes) == null);

		WebElement objRadioButtonProd = null;
		String xPathRB = numRefTxIntxpath.replace("NUM_REF_RECI_INTERNACIONAL", numRef).replace("MONEDA_INTERNACIONAL",
				moneda);

		objRadioButtonProd = this.element(By.xpath(xPathRB));

		contador = 0;
		do {
			DXCUtil.wait(1);
			contador++;
			if (contador >= 30) {
				if (this.element(sesionEx) != null) {
					String msg = this.element(sesionEx).getText();
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}

				return "TimeOut No se presentó el modulo";
			}

		} while (!this.isEnabled(By.xpath("//button[2]")));
		// -----------------------------------------------------------------------------------------------------------------------

		if (this.element(objRadioButtonProd) == null) {
			Evidence.save("Error-ProdOrigen");
			this.click(ButtonSigIn);
			String msg = this.closeActiveIntAlertConfirma();
			Reporter.reportEvent(Reporter.MIC_FAIL, msg);

			return "Número de refencia Externa [" + numRef + " - " + moneda + "] NO encontrado";
		}

		WebElement xpathOrdenante = null;

		String xphatd = IntxpathOrdenante.replace("NUM_REF_RECI_INTERNACIONAL", numRef).replace("MONEDA_INTERNACIONAL",
				moneda);

		xpathOrdenante = this.element(By.xpath(xphatd));

		// Primero valida si el elemento existe
		if (xpathOrdenante != null) {

			String ordenante = this.getText(xpathOrdenante);
			// Valida si el texto no es nulo ni vacío (o solo espacios)
			if (isValid(ordenante)) {
				SettingsRun.getTestData().setParameter("Ordenante / Nombre del beneficiario en el exterior", ordenante);
			} else {
				Evidence.save("Error-ProdOrigen");
				return "No se muestra dato del ordenante: [" + numRef + " - " + moneda + " - " + ordenante
						+ "] NO encontrado";
			}
		} else {
			Evidence.save("Error-ProdOrigen");
			return "No se muestra dato del ordenante: [" + numRef + " - " + moneda + "] NO encontrado";
		}

		contador = 0;

		do {
			DXCUtil.wait(1);
			contador++;
			if (contador >= 30) {
				if (this.element(sesionEx) != null) {
					String msg = this.element(sesionEx).getText();
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}
				return "TimeOut No se presentó el producto origen";
			}
		} while (!objRadioButtonProd.isDisplayed() || !objRadioButtonProd.isEnabled());

		// SI LLEGA A ESTE ES PORQUE EXISTE EL PRODUCTO ORIGEN DE LOS FONDOS

		xpathMonto = xpathMonto.replace("NUM_REF_RECI_INTERNACIONAL", numRef).replace("MONEDA_INTERNACIONAL", moneda);


		WebElement MontoElemnto = this.element(xpathMonto);

		if (MontoElemnto != null) {
			
			String montoTexto = MontoElemnto.getText();
			
			if (isValid(montoTexto)) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Se encuentra valor del Monto: " + montoTexto);
				// Normaliza el número: elimina puntos de miles y cambia coma decimal a punto
				valorMonto = Float.parseFloat(montoTexto.replace(".", "").replace(",", "."));
				
			} else {
				
				Reporter.reportEvent(Reporter.MIC_FAIL, "El elemento monto está vacío");
				valorMonto = 0;
			}
			
		} else {
			
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra valor del Monto");
			valorMonto = 0;
		}

		this.click(objRadioButtonProd);

		return null;

	}

// =======================================================================================================================

	public String seleccionarCuenta(String tipoDocumento,String numeroDoc,String tipoProducto, String numeroProducto) throws Exception {

		contador = 0;
		WebElement elementPaso;
		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA PARA LA SELECCI�N DEL ORIGEN
			DXCUtil.wait(1);
			elementPaso = this.element(tituMoAbono);
			contador++;
			if (contador >= 30) {
				if (this.element(sesionEx) != null) {
					String msg = this.element(sesionEx).getText();
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}

				return "TimeOut No se presentó el modulo";
			}
		} while (elementPaso == null);

		// -----------------------------------------------------------------------------------------------------------------------
		String tipoProdUpper = tipoProducto.toUpperCase();
		String tipoProd = "CORRIENTE"; // VALOR POR DEFECTO
		if (tipoProdUpper.contains("AHORROS"))
			tipoProd = "AHORROS";

		WebElement objRadioButtonProd = null;
		String xPathRB = cuentaDesTxIntxpath.replace("NUM_PROD", DXCUtil.right(numeroProducto, 4)).replace("TIPO_PROD",tipoProd);
		
		objRadioButtonProd = this.element(xPathRB);

		// -----------------------------------------------------------------------------------------------------------------------

		if (objRadioButtonProd == null) {
//				Evidence.saveFullPage("Error-ProdOrigen", this);
			Evidence.save("Error-ProdOrigen");
			return "Producto  [" + tipoProducto + " - " + numeroProducto + "] NO encontrado";
		}

		contador = 0;
		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA PARA LA SELECCI�N DEL ORIGEN
			DXCUtil.wait(1);
			contador++;
			if (contador >= 30) {
				if (this.element(sesionEx) != null) {
					String msg = this.element(sesionEx).getText();
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}

				return "TimeOut No se presentó el modulo";
			}

		} while (!objRadioButtonProd.isDisplayed() || !objRadioButtonProd.isEnabled());

		// SI LLEGA A ESTE ES PORQUE EXISTE EL PRODUCTO ORIGEN DE LOS FONDOS
		this.click(objRadioButtonProd);
//			Evidence.saveFullPage("Producto selecionado", this);
		Evidence.save("Producto selecionado");

		if (!DatosDavivienda.IS_RISKMOTOR)
			this.validacionSaldosStratus(tipoDocumento, numeroDoc,tipoProd, numeroProducto, true);

		contador = 0;
		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA PARA LA SELECCI�N DEL ORIGEN
			DXCUtil.wait(1);
			contador++;
			if (contador >= 30) {

				if (this.element(sesionEx) != null) {
					String msg = this.element(sesionEx).getText();
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}

				return "TimeOut No se presentó el boton siguiente";

			}
		} while (!this.isDisplayed(ButtonSigIn) || !this.isEnabled(ButtonSigIn));

		this.click(ButtonSigIn);

		String msError = this.closeActiveIntAlert();

		if (isValid(msError)) {
			this.terminarIteracion();
		}

		return null;
	}

// =======================================================================================================================

	public String Cotizacion() throws Exception {
		contador = 0;
		WebElement elementPaso;
		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA
			DXCUtil.wait(1);
			elementPaso = this.element(tituMoCotizar);
			contador++;
			if (contador >= 30) {
				if (this.element(sesionEx) != null) {
					String msg = this.element(sesionEx).getText();
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}
				String msError = this.getMsgAlertIfExist();
				if (isValid(msError)) {
					Reporter.reportEvent(Reporter.MIC_FAIL, msError);
				}

				return "TimeOut No se presentó el modulo";
			}
			
		} while (elementPaso == null);

		Evidence.save("Cotizacion");
		contador = 0;
		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA
			DXCUtil.wait(1);
			elementPaso = this.element(ButtonSigIn);
			contador++;
			if (contador >= 30) {
				if (this.element(sesionEx) != null) {
					String msg = this.element(sesionEx).getText();
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}

				return "TimeOut No se presentó el botón siguiente";
			}
		} while (this.element(elementPaso) == null);

		/*
		 * Valida la catidad de decimales para la pantalla cotizacion Verifica que el
		 * formato numerico sea el correcto
		 */
		int i = 1;
		do {
			DXCUtil.wait(1);
			i++;
			String xpathCotiza = xpathCamposCotiza.replace("NUMERO_FILA", Integer.toString(i));
			String textCotiza = this.element(xpathCotiza).getText();
			String arrayCotiza[] = textCotiza.split(",");

			int decimales = arrayCotiza[1].length();

			if (i == 2 && decimales == 2) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Formato correcto en Monto");
			} else if (i == 3 && decimales == 4) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Formato correcto en Tipo de cambio a USD");
			} else if (i == 4 && decimales == 2) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Formato correcto en Monto en USD");
			} else if (i == 5 && decimales == 2) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Formato correcto en Tasa de cambio");
			} else if (i == 6 && decimales == 2) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Formato correcto en Valor de la operación en COP");
			} else if (i == 7 && decimales == 2) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Formato correcto en Costo del servicio");
			} else if (i == 8 && decimales == 2) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Formato correcto en IVA sobre costo del servicio");
			} else if (i == 9 && decimales == 2) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Formato correcto en Valor Neto a recibir");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,"Formato incorrecto en:  " + this.element(xpathCotiza).getText());
			}
		} while (i < 9);

		this.click(ButtonSigIn);

		return null;
	}

// =======================================================================================================================

	/**
	 * Garantiza que se encuentra en la pantalla Confirmación
	 * 
	 * @return
	 * @throws Exception
	 */
	public String Confirmacion() throws Exception {
		contador = 0;
		do {
			DXCUtil.wait(1);
			contador++;
			if (contador >= 30) {
				if (this.element(sesionEx) != null) {
					String msg = this.element(sesionEx).getText();
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}

				return "TimeOut no se encuentra el subTitulo de Conformación";
			}
		} while (this.element(titMoInformacion) == null);

		Evidence.save("Confirmación");

		if (this.isDisplayed(cmpMonto)) {
			String monto = this.getText(cmpMonto).trim();
			if (isValid(monto)) {
				SettingsRun.getTestData().addParametersNotExist("Monto Tx");
				SettingsRun.getTestData().setParameter("Monto Tx", monto);
			}

		} else {
			Reporter.reportEvent(Reporter.MIC_INFO, "No se presenta el campo: Monto");
		}

		if (this.isDisplayed(cmpTipodecambioaUSD)) {
			String tipodecambioaUSD = this.getText(cmpTipodecambioaUSD).trim();
			if (isValid(tipodecambioaUSD)) {
				SettingsRun.getTestData().addParametersNotExist("Tipo de cambio a USD");
				SettingsRun.getTestData().setParameter("Tipo de cambio a USD", tipodecambioaUSD);
			}
			SettingsRun.getTestData().setParameter("Tipo de cambio a USD", tipodecambioaUSD);

		} else {
			Reporter.reportEvent(Reporter.MIC_INFO, "No se presenta el campo: Tipo de cambio a USD");
		}

		if (this.isDisplayed(cmpMontoenUSD)) {
			String montoenUSD = this.getText(cmpMontoenUSD).trim();
			if (isValid(montoenUSD)) {
				SettingsRun.getTestData().addParametersNotExist("Monto en USD");
				SettingsRun.getTestData().setParameter("Monto en USD", montoenUSD);
			}
			SettingsRun.getTestData().setParameter("Monto en USD", montoenUSD);

		} else {
			Reporter.reportEvent(Reporter.MIC_INFO, "No se presenta el campo: Monto en USD");
		}

		if (this.isDisplayed(cmpTasadecambio)) {
			String tasadecambio = this.getText(cmpTasadecambio).trim();
			if (isValid(tasadecambio)) {
				SettingsRun.getTestData().addParametersNotExist("Tasa de cambio");
				SettingsRun.getTestData().setParameter("Tasa de cambio", tasadecambio);
			}
			SettingsRun.getTestData().setParameter("Tasa de cambio", tasadecambio);

		} else {
			Reporter.reportEvent(Reporter.MIC_INFO, "No se presenta el campo: Tasa de cambio");
		}
		if (this.isDisplayed(cmpVapesoscolombianos)) {
			
			String vapesoscolombianos = this.getText(cmpVapesoscolombianos).trim();
			
			if (isValid(vapesoscolombianos)) {
				SettingsRun.getTestData().addParametersNotExist("Valor de la operación en pesos colombianos");
				SettingsRun.getTestData().setParameter("Valor de la operación en pesos colombianos", vapesoscolombianos);
			}
			SettingsRun.getTestData().setParameter("Valor de la operación en pesos colombianos", vapesoscolombianos);

		} else {
			Reporter.reportEvent(Reporter.MIC_INFO,"No se presenta el campo: Valor de la operación en pesos colombianos");
		}

		if (this.isDisplayed(cmpCostoServicio)) {
			String costoServicio = this.getText(cmpCostoServicio).trim();
			
			if (isValid(costoServicio)) {
				SettingsRun.getTestData().addParametersNotExist("Costo del servicio");
				SettingsRun.getTestData().setParameter("Costo del servicio", costoServicio);
			}
			
			SettingsRun.getTestData().setParameter("Costo del servicio", costoServicio);

		} else {
			Reporter.reportEvent(Reporter.MIC_INFO, "No se presenta el campo: Costo del servicio");
		}

		if (this.isDisplayed(cmpIVAservicio)) {
			String iVAservicio = this.getText(cmpIVAservicio).trim();
			if (isValid(iVAservicio)) {
				SettingsRun.getTestData().addParametersNotExist("IVA sobre costo del servicio");
				SettingsRun.getTestData().setParameter("IVA sobre costo del servicio", iVAservicio);
			}
			SettingsRun.getTestData().setParameter("IVA sobre costo del servicio", iVAservicio);

		} else {
			Reporter.reportEvent(Reporter.MIC_INFO, "No se presenta el campo: IVA sobre costo del servicio");
		}

		if (this.isDisplayed(cmpValornetoarecibir)) {
			String valornetoarecibir = this.getText(cmpValornetoarecibir).trim();
			if (isValid(valornetoarecibir)) {
				SettingsRun.getTestData().addParametersNotExist("Valor Neto a recibir");
				SettingsRun.getTestData().setParameter("Valor Neto a recibir", valornetoarecibir);
			}
			SettingsRun.getTestData().setParameter("Valor Neto a recibir", valornetoarecibir);

		} else {
			Reporter.reportEvent(Reporter.MIC_INFO, "No se presenta el campo: Valor Neto a recibir");
		}
		return null;
	}
	

// =======================================================================================================================

	/**
	 * Realiza la validacion de los saldos Iniciales en Stratus comparando los saldo
	 * Totales del Portal
	 * 
	 * @param tipoCta
	 * @param numCta
	 * @param saldoOrigen
	 * @throws Exception
	 */
	public void validacionSaldosStratus(String tipoDocumento,String numeroDoc,String tipoCta, String numCta, boolean saldoOrigen) throws Exception {
		if (DatosDavivienda.STRATUS != null) {
			String saldoDis = null;
			String saldoDisponible = null;
			if (saldoOrigen) {
				saldoDis = this.getText(this.element(locSaldodisponible.replace("NUMCUENTA", DXCUtil.right(numCta, 4)).replace("TIPOCUENTA", tipoCta)));
				saldoDisponible = saldoDis.replace("$ ", "").replace(".", "").replace(",", ".");
			}

			if (!DatosDavivienda.IS_RISKMOTOR) {
				if (tipoCta.contains("AHORROS") || tipoCta.contains("ahorros") || tipoCta.contains("Ahorros")|| tipoCta.contains("Ahorro"))
					tipoCta = "AHORROS";
				else if (tipoCta.contains("CORRIENTE") || tipoCta.contains("corriente")|| tipoCta.contains("Corriente")) 
					tipoCta = "CORRIENTE";
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

					// Elimina el carater '$' y espacios de ambas cadenas antes de formatear
					if (tipoCta.contains("ahorro") || tipoCta.contains("AH")) {
						this.saldoTotalInicial = saldosDisponibles[0].replace("$ ", "");
						this.saldoDispoInicial = saldosDisponibles[1].replace("$ ", "");
					}
					if (tipoCta.contains("corriente") || tipoCta.contains("CC") || tipoCta.contains("CORRIENTE")) {
						this.saldoTotalInicial = saldosDisponibles[6].replace("$ ", "");
						this.saldoDispoInicial = saldosDisponibles[0].replace("$ ", "");

					}
					if (tipoCta.contains("CREDITO") || tipoCta.contains("diplus")) {
						this.saldoTotalInicial = saldosDisponibles[0].replace("$ ", "");
						this.saldoDispoInicial = saldosDisponibles[6].replace("$ ", "");

					}
					Reporter.reportEvent(Reporter.MIC_INFO,
							"El saldo stratus Total Inicial: " + this.saldoTotalInicial.replace(".", ","));
					Reporter.reportEvent(Reporter.MIC_INFO, "El saldo stratus Disponible Efectivo Inicial: "
							+ this.saldoDispoInicial.replace(".", ","));
				} else if (!saldoOrigen) {

					// Elimina el carater '$' y espacios de ambas cadenas antes de formatear
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

// =======================================================================================================================

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
// =======================================================================================================================
	
	public void setTime(Date fechaTxTemp) throws Exception {
		String today = "";
		String hour = "";
		if (fechaTxTemp != null) {
			today = DXCUtil.dateToString(fechaTxTemp, "dd/mm/yyyy");
			hour = DXCUtil.hourToString(fechaTxTemp, "HH:mm");
			hour = this.convertTime(hour);
		}
		SettingsRun.getTestData().setParameter("Fecha tx", today);
		SettingsRun.getTestData().setParameter("Hora tx", hour);

	}

	private String convertTime(String fechaConverTime) throws Exception {
		SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm");
		SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm");

		Date date = inputFormat.parse(fechaConverTime);
		String convertedTime = outputFormat.format(date);
		return convertedTime;
	}
}
