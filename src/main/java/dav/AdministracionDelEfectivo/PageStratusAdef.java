package dav.AdministracionDelEfectivo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JOptionPane;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import dav.ActualizacionDeDatos.PageActualizacionDeDatos;

import dav.middlePymes.PageInicioMiddle;
import dav.AdministracionDelEfectivo.*;
import dav.pymes.PageLoginPymes;
import dav.pymes.moduloCrearTx.*;
import dav.transversal.DatosDavivienda;
import dav.transversal.DatosEmpresarial;
import dav.transversal.MovimientoStratus;
import dav.transversal.Stratus;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Reporter;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;
import dxc.transversal.Putty;
import launchTest.LaunchTestPyme;

public class PageStratusAdef extends Putty {
	
	Properties info;
	
	private final String PT_CONSULTA_MOVIMIENTO = "C O N S U L T A   M O V I M I E N T O";
	
	public void InicioStratusAdef() throws Exception {

		info = new Properties();
		String propNbFile = "data.properties";

		if (!DXCUtil.PATH_RESOURCES.isEmpty()) {
			String temp = new File(".").getAbsolutePath(); // VIENE POR EJEMPLO: [C:/Users/userX/Desktop/.]
			String pathTestFilesDir = DXCUtil.left(temp, temp.length() - 1); // LE QUITA EL PUNTO FINAL
			String nbFileProperties = pathTestFilesDir + propNbFile;
			File propFile = new File(nbFileProperties);
			info.load(new FileInputStream(propFile));
		} else { // EL LANZAMIENTO ES POR IDE
			
			String source = "/" + propNbFile;
			info.load(LaunchTestPyme.class.getResourceAsStream(source));
		}
		
		String strusu = info.getProperty("STRATUS.usuario");
		String strpass = info.getProperty("STRATUS.password");
		DatosDavivienda.STRATUS = new Stratus(strusu, strpass, "EMPRESAS");
		
	}
	
	public String consultaStratusAdef(String tipoConsulta, String tipoProducto, String numCuenta, String[] dataAprob) throws Exception {
		
//		Date fechaConsulta;
////		DatosDavivienda.STRATUS.irConsultaPortafolio();
//		
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.DAY_OF_MONTH, - 1);
//		
//		calendar.set(Calendar.HOUR_OF_DAY, 9);
//		calendar.set(Calendar.MINUTE, 14);
////		Date fechaActual = new Date();
//		
//		Date fechaActual = calendar.getTime();
//        SimpleDateFormat formatoFecha = new SimpleDateFormat("MMdd HHmm");
//        
//        String fecha = formatoFecha.format(fechaActual);
////		String fechaIngresar = DXCUtil.dateToString(fechaActual, "mmdd hhmm ");
//		
//		String[] arrFechaHora = Stratus.getArrayAll_FsiHoraMov(fechaActual, 3);
		
//		if (!tipoProducto.equals("OTM")) {
			
//		String[] msg = DatosDavivienda.STRATUS.consultarDatosPantallaSaldos("Nit", "8300035425", "CC", "0560099869989923");
		
//		} else {
//			
//			this.sendKeys(1, Keys.CONTROL, "x");
//			this.sendKey(4);
//			
//		}
		
//		String[] arrayVents = { MovimientoStratus.VENT_UNID };
		
		String[] datosTitular = { "CÉDULA DE CIUDADANÍA", "11111109"};

//		for (String datos:msg) {
//			System.out.println(datos);
//		}
	
//		DatosDavivienda.STRATUS.getMovimientos("CC", "0560099869989923", arrayVents, fechaActual, arrFechaHora, datosTitular);
		
//		WebElement getextoFecha = this.element(fechaTx);
		
					// -----  AGREGAR LA FECHA MANUAL
		
			//		SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			//
			//		String FechaTx = "08-05-2024";
			//
			//		String horaTxMov = "09:31:00";
			//
			//		String fechaHora = FechaTx + " "+horaTxMov;
			//		Date fechaConsulta = formatoFecha.parse(fechaHora);
					
					// -----  AGREGAR LA FECHA MANUAL
		
		System.out.println("1" + dataAprob[0]);
		
		SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		dataAprob[0] = dataAprob[0].replace("/", "-");
		dataAprob[0] = dataAprob[0] + ":00";
		Date fechaHorConsulta = formatoFecha.parse(dataAprob[0]);
		
//		String fecha = DXCUtil.dateToString("MMDD");
//		
//		horaTxMov = DXCUtil.hourToStringFormat(horaTxMov, "HHmm");
//		String horaTxMov1 = DXCUtil.horaAdd(horaTxMov, -1);
//		String[] fechaHora2 = { fecha + " " + horaTxMov1 };
//      
		// Datos del titular como un array de String Stratus

//		String tipoDocumento = SettingsRun.getGlobalData().getParameter("Tipo Identificación");
//		String numeroDoc = SettingsRun.getGlobalData().getParameter("Id usuario").trim();
//		String tipoProduct = SettingsRun.getGlobalData().getParameter("Tipo producto origen / Franquicia")
//				.trim();
//		String numeroProducto = SettingsRun.getGlobalData().getParameter("Número producto origen").trim();
//
//		// Datos usuario
//		String[] datosTitular = { tipoDocumento, numeroDoc };
		// Obtiene el tipo de cuenta si es migrada o fondo o tarjeta de cradito
//		String tipoProd = Stratus.getTipoCuenta(tipoProduct);
		
		// Ventanas en consulta de movimientos Stratus
//		String[] arrayVents = { MovimientoStratus.VENT_UNID, MovimientoStratus.VENT_IVA,
//				MovimientoStratus.VENT_SALDO, MovimientoStratus.VENT_EMERG };

		String[] arrayVents = { MovimientoStratus.VENT_IVA};
		/**

			// Obtiene la ultima tabla de los movimientos masivos
			this.registrosmov = RegistrosMovimientos();
			for (String remov : this.registrosmov) {
				String[] registoDestinos = remov.split("\\|");
				this.tipoDes = registoDestinos[1];
				this.estadodes = registoDestinos[3];
			}
			**/

		// Validacion con Stratus
		if (DatosDavivienda.STRATUS != null) {
			
			// Realiza la consulta en Stratus de todos los movimientos
			
			System.out.println("2"+fechaHorConsulta);

			List<MovimientoStratus> datosCtadh2 = DatosDavivienda.STRATUS.getMovimientosEnRangoStratus(tipoProducto,
					numCuenta, arrayVents, fechaHorConsulta, 1, datosTitular);

			if (datosCtadh2 == null) {
				return null;
			}
//			List<MovimientoStratus> datosCtadh2 = DatosDavivienda.STRATUS.getMovimientos("CC", "0560099869989923", arrayVents, FechaConsultaStratus, fechaHora2, datosTitular);

			List<String[]> movimientosStratus = new ArrayList<>();

			// Saldo inicial
//			double saldoTotalInicial = 0.0;
//			double saldoDisponible = 0.0;
//			saldoTotalInicial = Double.parseDouble(saldoinicialstra);
//			String saldoPos = String.format("%.1f", saldoTotalInicial);
//			saldoDisponible = Double.parseDouble(saldoDislstra);
//
//			// Saldo final
//			double saldoTotalFinal = 0.0;
//			double saldoDisponibleFinal = 0.0;
//			saldoTotalFinal = Double.parseDouble(saldofinal);
//			saldoDisponibleFinal = Double.parseDouble(saldoDisponiblefinal);
//			String saldoPosFinal = String.format("%.2f", saldoTotalFinal);
//			Reporter.write(" ");
//			Reporter.write("*** CONSULTA DE MOVIMIENTOS EN STRATUS");
//			Reporter.write(" ");
			
			// Retorna los movimientos en Stratus
			for (MovimientoStratus movimiento : datosCtadh2) {
				String oficinaStratus = null, tipoStratus = null, mtvoStratus = null, talon = null;
				double ValorTotalMov = 0.0, ValorChequeMovStartus = 0.0;
				double saldoAnteriorStratus = 0.0;
				String movToString = movimiento.toString().replace("[INDIC:", "").replace("]", "")
						.replace("SALDO:", "|").replace("UNID :", "|").replace("EMERG:", "|")
						.replace("IVA  :", "|").replace(" ", "");
				String[] datos = movToString.split("\\|");// Extraer los datos necesarios por posiciones
				String fecha = datos[0].trim();
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

				String saldoUnidad = "0.0";
				String chequeStratus = "0.0";
				String in = "0.0";
				String valorEmerg = "0.0";
				String indCancelado = "0.0";

				String ivam = "0.0";
				
				try {
					
					 saldoUnidad = datos[10].trim();
					 chequeStratus = datos[11];
					 in = datos[12];
					 valorEmerg = datos[13];
					 indCancelado = datos[14];

					 ivam = datos[15];
					
				} catch (Exception e) {
					
					 saldoUnidad = "0.0";
					 chequeStratus = "0.0";
					 in = "0.0";
					 valorEmerg = "0.0";
					 indCancelado = "0.0";

					 ivam = "0.0";
					
				}

				String msgcancelado = "";
				dataAprob[1] = dataAprob[1].replace("$", "").replace(",", "");
				dataAprob[3] = dataAprob[3].replace("$", "").replace(",", "");

				if (indCancelado.equals("true")) {
					msgcancelado = "Cancelado";
				}

//				Reporter.write("Movimiento: " + fecha + " | " + hora + " | " + talon + " | " + tipoStratus
//						+ "  |  " + oficinaStratus + " | " + BigDecimal.valueOf(ValorTotalMov) + " | "
//						+ mtvoStratus + "  |  " + saldoUnidad + "  |  "
//						+ BigDecimal.valueOf(ValorChequeMovStartus) + "  |  " + ivam + "  |  " + saldoAnterior
//						+ "  |  " + valorEmerg + " | " + msgcancelado);

				String movr = fecha + " " + hora + " " + talon + " " + tipoStratus + " " + oficinaStratus + " "
						+ BigDecimal.valueOf(ValorTotalMov) + " " + mtvoStratus + " " + saldoUnidad + " "
						+ BigDecimal.valueOf(ValorChequeMovStartus) + " " + ivam + " " + saldoAnterior + " "
						+ valorEmerg + " " + indCancelado;
				
				if (tipoConsulta.equals("Transportadora") || tipoConsulta.equals("Cuentas Destino")) {
					if ((movr.contains("0034") || movr.contains("0055") || movr.contains("0099"))) {
						movimientosStratus.add(movr.split(","));
					}
				}
				
				if (tipoConsulta.equals("Cobro Distribucion")) {
					
					double saldoTotal;

					saldoTotal = Double.parseDouble(String.valueOf(ValorTotalMov)) + Double.parseDouble(saldoAnterior);
					
					if (saldoTotal <= Double.parseDouble(dataAprob[3]) && saldoTotal >= Double.parseDouble(dataAprob[3])-1) {
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

			System.out.println("Cris" + dataAprob[1].substring(0, dataAprob[1].length()-1));
				// Encuentra la posición del saldo inicial en movimientosStratus
			if (tipoConsulta.equals("Transportadora") || tipoConsulta.equals("Cuentas Destino")) {
				
				for (int i = 0; i < movimientosStratus.size(); i++) {
					String[] movimiento = movimientosStratus.get(i);
					if (movimiento[movimiento.length - 1]
							.contains(dataAprob[1].substring(0, dataAprob[1].length()-1)) && movimiento[movimiento.length - 1]
									.contains(dataAprob[2].replace(":", ""))) {
						posicionSaldoInicialpos = i;
						break;
					}
				}

			} else if (tipoConsulta.equals("Cobro Distribucion")) {
				
				for (int i = 0; i < movimientosStratus.size(); i++) {
					String[] movimiento = movimientosStratus.get(i);
					if (movimiento[movimiento.length - 1]
									.contains(dataAprob[2].replace(":", ""))) {
						posicionSaldoInicialpos = i;
						break;
					}
				}
				
			}
			
//			}	break;
				
			/**
				posicionSaldoInicialpos = movimientosStratus.size() - 1;
**/	
			

//			Reporter.write(" ");
//			Reporter.write("*** VALIDACIÓN DEL MOVIMIENTO");
//			Reporter.write(" ");

			if (posicionSaldoInicialpos != -1) {

					String[] movimiento = movimientosStratus.get(posicionSaldoInicialpos);
					String[] datosMov = movimiento[0].split(" ");

					String fecha = datosMov[0].trim();
					String hora = datosMov[1].trim();
					String tipoMovimiento = datosMov[3].trim();
					String oficina = datosMov[4].trim();
					double valorMovimiento = Double.parseDouble(datosMov[5].trim().replace(",", ""));
					String concepto = datosMov[6].trim();
					double valorunida = Double.parseDouble(datosMov[7].trim().replace(",", ""));
					double valorCheque = Double.parseDouble(datosMov[8].trim().replace(",", ""));
					double valorIva = Double.parseDouble(datosMov[10].trim().replace(",", ""));
					double emerg = Double.parseDouble(datosMov[11].trim().replace(",", ""));
					String cancelados = datosMov[12].trim();
					
					if (tipoConsulta.equals("Transportadora")) {

							for (String data:movimiento) {
								Reporter.write("stratus transportadora_movimiento: " + data);
							}
							
							if (tipoProducto.equals("OTM")) {
								Reporter.write("stratus transportadora_coincide en fecha y hora " + fecha + " " + hora);
								Reporter.write("stratus transportadora_coincide en saldo: " +  valorMovimiento);
							}
							
							if (concepto.equals("273") && tipoProducto.equals("OTM")) {
								
								Reporter.write("stratus transportadora_coincide en concepto: " + concepto);
							} else {
								Reporter.write("stratus transportadora_no coincide en concepto: " + concepto);
							}
		
							if (tipoMovimiento.equals("0099") && tipoProducto.equals("OTM")) {
								Reporter.write("stratus transportadora_coincide en tipo de movimiento: " + tipoMovimiento);
							} else {
								Reporter.write("stratus transportadora_no coincide en tipo de movimiento: " + tipoMovimiento);
							}
							
							if (oficina.equals("4844") && tipoProducto.equals("OTM")) {
								Reporter.write("stratus transportadora_coincide en oficina: " + oficina);
							} else {
								Reporter.write("stratus transportadora_no coincide en oficina: " + oficina);
							}
							
					} else if (tipoConsulta.equals("Cobro Distribucion")) {
							
							for (String data:movimiento) {
								Reporter.write("stratus cobro distribucion_movimiento: " + data);
							}
							
							Reporter.write("stratus cobro distribucion_coincide en fecha y hora: " + fecha + " " + hora );
							Reporter.write("stratus cobro distribucion_coincide en saldo: " + valorMovimiento + " IVA" +" + " + valorIva + " GMF" + " <=> "  + dataAprob[3] );
							
							if (concepto.equals("741")) {
								
								Reporter.write("stratus cobro distribucion_coincide en concepto: " + concepto);
							} else {
								Reporter.write("stratus cobro distribucion_no coincide en concepto: " + concepto);
							}
		
							if (tipoMovimiento.equals("0055")) {
								Reporter.write("stratus cobro distribucion_coincide en tipo de movimiento: " + tipoMovimiento);
							} else {
								Reporter.write("stratus cobro distribucion_no coincide en tipo de movimiento: " + tipoMovimiento);
							}
							
							if (oficina.equals("4844")) {
								Reporter.write("stratus cobro distribucion_coincide en oficina: " + oficina);
							} else {
								Reporter.write("stratus cobro distribucion_no coincide en oficina: " + oficina);
							}
						
					} else if (tipoConsulta.equals("Cuentas Destino")) {
						
						for (String data:movimiento) {
							Reporter.write("stratus cuentas destino_movimiento: " + data);
						}
						
						Reporter.write("stratus cuentas destino_coincide en fecha y hora: " + fecha + " " + hora );
						Reporter.write("stratus cuentas destino_coincide en saldo: " + valorMovimiento );
						
						if (concepto.equals("754")) {
							
							Reporter.write("stratus cuentas destino_coincide en concepto: " + concepto);
						} else {
							Reporter.write("stratus cuentas destino_no coincide en concepto: " + concepto);
						}
	
						if (tipoMovimiento.equals("0034")) {
							Reporter.write("stratus cuentas destino_coincide en tipo de movimiento: " + tipoMovimiento);
						} else {
							Reporter.write("stratus cuentas destinon_no coincide en tipo de movimiento: " + tipoMovimiento);
						}
						
						if (oficina.equals("4844")) {
							Reporter.write("stratus cuentas destino_coincide en oficina: " + oficina);
						} else {
							Reporter.write("stratus cuentas destino_no coincide en oficina: " + oficina);
						}
					
					}
				
				}
			
//				Reporter.write("SUMA de Movimiento: " + BigDecimal.valueOf(sumaMovimientos));
//				Reporter.write("SUMA de Unidad: " + BigDecimal.valueOf(sumaunida));
//				Reporter.write("SUMA de Cheque: " + BigDecimal.valueOf(sumaCheque));
//				Reporter.write("SUMA de Iva: " + BigDecimal.valueOf(sumaIva));
//				Reporter.write("SUMA de GMF: " + BigDecimal.valueOf(sumaGmf));
//				Reporter.write(" ");
//				double restaMovimientos = 0.0;
//				double restaunida = 0.0;
//				double restaCheque = 0.0;
//				double restaIva = 0.0;
//				double restaGmf = 0.0;
//
//				// Restar movimientos cancelados y tipoMovimiento 307
//				for (int i = posicionSaldoInicialpos; i < movimientosStratus.size(); i++) {
//					String[] movimiento = movimientosStratus.get(i);
//					String[] datosMov = movimiento[0].split(" ");
//
//					double valorMovimiento = Double.parseDouble(datosMov[5].trim().replace(",", ""));
//					String tipoMovimiento = datosMov[6].trim();
//					double valorunida = Double.parseDouble(datosMov[7].trim().replace(",", ""));
//					double valorCheque = Double.parseDouble(datosMov[8].trim().replace(",", ""));
//					double valorIva = Double.parseDouble(datosMov[9].trim().replace(",", ""));
//					double emerg = Double.parseDouble(datosMov[11].trim().replace(",", ""));
//					String cancelados = datosMov[12].trim();
//
//					if (tipoMovimiento.equals("307")) {
//						restaMovimientos += valorMovimiento;
//						restaunida += valorunida;
//						restaCheque += valorCheque;
//						restaIva += valorIva;
//						restaGmf += emerg;
//					} else if (tipoMovimiento.equals("307") || cancelados.equals("true")) {
//						restaMovimientos += valorMovimiento;
//						restaunida += valorunida;
//						restaCheque += valorCheque;
//						restaIva += Math.round(valorIva);
//						restaGmf += emerg;
//					}
//				}
//
//				Reporter.write("SUMA de Movimiento cancelado: " + BigDecimal.valueOf(restaMovimientos));
//				Reporter.write("SUMA de Unidad cancelado: " + BigDecimal.valueOf(restaunida));
//				Reporter.write("SUMA de Cheque cancelado: " + BigDecimal.valueOf(restaCheque));
//				Reporter.write("SUMA de Iva cancelado: " + BigDecimal.valueOf(restaIva));
//				Reporter.write("SUMA de GMF cancelado: " + BigDecimal.valueOf(restaGmf));
//				Reporter.write(" ");
//
//				double valorMov = 0.0;
//				valorMov = (sumaMovimientos - restaMovimientos);
//				double valorIva = 0.0;
//
//				valorIva = (sumaIva - restaIva);
//				double valorGmf = 0.0;
//
//				valorGmf = (sumaGmf - restaGmf);
//
//				Reporter.write(" ");
//				Reporter.write("*** HACIENDO LAS VALIDACIÓN DEL MOVIMIENTO");
//				Reporter.write(" ");
//				Reporter.write("Valor del Movimiento: " + BigDecimal.valueOf(valorMov));
//				Reporter.write("Valor del Iva: " + BigDecimal.valueOf(valorIva));
//				Reporter.write("Valor del GMF " + BigDecimal.valueOf(valorGmf));
//
//				double costoTx = 0.0;
//				costoTx = (sumaMovimientos + restaMovimientos);
//
//				// Restar los montos al saldo inicial
//				double saldoesperado = 0.0;
//				saldoesperado = (saldoTotalInicial - valorMov - Math.round(valorIva) - valorGmf);
//
//				saldoTotalFinalEsperado = saldoesperado;
//
//				// Calcular la diferencia entre los saldos finales
//				double diferencia = 0.0;
//
//				diferencia = (saldoTotalFinal - saldoTotalFinalEsperado);
//				Reporter.write(" ");
//				// Mostrar los resultadosl
//
//				Reporter.reportEvent(Reporter.MIC_INFO, "La diferencia entre los saldos final es: "
//						+ BigDecimal.valueOf(Math.round(diferencia)));
//
//			} else {
//
//				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontró el saldo inicial en los movimientos.");
//
//			}

//		}
	
		
//		DatosDavivienda.STRATUS.ctrlX(4); // F4 - MOVIMIENTOS : PARA CONSULTAR LOS MOVIMIENTOS
//		String textoEnPantalla = DatosDavivienda.STRATUS.read(PT_CONSULTA_MOVIMIENTO); // ESPERA A QUE MUESTRE LA PANTALLA DE MOVIMIENTOS
//		
////		if (fechaIngresar.equals(fecha))
////			this.tab(3); // SE MUEVE AL CAMPO "VENT"
////		else { // SE DEBE CAMBIAR LA FECHA
////			this.tab(2);
////			this.paste(fechaIngresar); // DEJA EL CURSOR EN EL CAMPO "VENT"
////		}
//		
//		DatosDavivienda.STRATUS.ingresarDatosCta("AHD","");
//		
//		DatosDavivienda.STRATUS.ctrlX(1); // POR DEFECTO EL PRIMER VENT ES [MovimientoStratus.VENT_INDIC
//		
//		String[] arrFechaHora = Stratus.getArrayAll_FsiHoraMov(fechaActual, 0);
//		textoEnPantalla = DatosDavivienda.STRATUS.ubicarPantallaCon1erMovimientoBuscado(arrFechaHora, true);
//		
//		String lastPantalla;
//		if (textoEnPantalla == null)
//			return null;
//		
		
	}
		if (tipoProducto.equals("OTM")) {
			DatosDavivienda.STRATUS.selectOpcionMenuLista(0);
		}

		return "ok";
		
}
}