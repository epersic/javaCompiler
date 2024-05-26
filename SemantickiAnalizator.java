
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SemantickiAnalizator {
	static Cvor root = null;
	static TablicaZnakova globalna = new TablicaZnakova(null);

	public static void main(String[] args) throws FileNotFoundException {
		parseInput("C:\\Users\\Fran\\eclipse-workspace\\ppjLab3\\src\\ppjLab3\\test4.txt");
		boolean tf = prijevodnaJedinica(root);
		//System.out.println(tf);
		if(!tf) return;

		boolean main=false;
		for(int i=0;i<globalna.znak.size();i++) {
			if(globalna.getZnak(i).equals("main") && globalna.getTip(i).equals("void->int") && globalna.getDef(i)==true) {
				main=true;
				break;
			}
		}
		if(!main) {
			System.out.println("main");
			return;
		}

		if(globalna.postojiNeDefinirana(globalna)) {
			System.out.println("funkcija");
			return;
		}

	}

	private static void parseInput(String file) throws FileNotFoundException {
		File myFile = new File(file);
		Scanner sc = new Scanner(System.in);

		Map<Integer, Cvor> zadnjiCvor = new HashMap<>();
		while(sc.hasNextLine()) {
			String line = sc.nextLine();

			int dubina = line.length()-line.trim().length();
			String znak = line.trim();
			Cvor noviCvor = new Cvor(znak);

			zadnjiCvor.put(dubina, noviCvor);

			Cvor roditelj = zadnjiCvor.get(dubina-1);
			if(roditelj==null) {
				root = noviCvor;
				noviCvor.tablicaZnakova = globalna;
				continue;
			}
			noviCvor.roditelj = roditelj;
			if(znak.equals("<slozena_naredba>")) {
				noviCvor.tablicaZnakova = new TablicaZnakova(noviCvor.roditelj.tablicaZnakova);
			}else {
				noviCvor.tablicaZnakova = noviCvor.roditelj.tablicaZnakova;
			}
			if(znak.charAt(0)!='<') {
				String[] split = znak.split(" ");
				noviCvor.znak=split[0];
				noviCvor.svojstva.put("linija", split[1]);
				noviCvor.svojstva.put("lJedinka", split[2]);
			}
			roditelj.djeca.add(noviCvor);
		}
		sc.close();
	}

	private static String ispisZavrsnog(Cvor zavrsni) {
		return "(" + zavrsni.svojstva.get("linija") + "," + zavrsni.svojstva.get("lJedinka") + ")";
	}

	private static boolean provjeriProdukciju(Cvor cvor,String... args){
		int l = args.length;
		if(cvor.djeca.size()!=l) return false;
		for(int i=0;i<l;i++) {
			if(!cvor.djeca.get(i).znak.equals(args[i])) return false;
		}
		return true;
	}

	private static boolean castU(String tip1, String tip2) {
		if(tip1==null || tip2==null) return false;
		tip1=tip1.trim();
		tip2=tip2.trim();

		if(tip1.equals("const(int)") && tip2.equals("int")) return true;
		if(tip1.equals("const(char)") && tip2.equals("char")) return true;

		if(tip1.equals("int") && tip2.equals("const(int)")) return true;
		if(tip1.equals("char") && tip2.equals("const(char)")) return true;

		if(tip1.equals("char") && tip2.equals("int")) return true;

		if(tip1.equals("niz(int)") && tip2.equals("niz(const(int))")) return true;
		if(tip1.equals("niz(char)") && tip2.equals("ciz(const(char))")) return true;

		if(tip1.equals("const(char)") && tip2.equals("int")) return true;
		if(tip1.equals("const(char)") && tip2.equals("const(int)")) return true;
		if(tip1.equals("char") && tip2.equals("const(int)")) return true;
		if(tip1.equals(tip2)) return true;

		return false;
	}

	private static Cvor pridUniz(Cvor izrazPridruzivanja) {
		Cvor ili = izrazPridruzivanja.djeca.get(0);
		if(ili.znak.equals("<log_ili_izraz>") && izrazPridruzivanja.djeca.size()==1) {
			Cvor i = ili.djeca.get(0);
			if(i.znak.equals("<log_i_izraz>") && ili.djeca.size()==1) {
				Cvor bili = i.djeca.get(0);
				if(bili.znak.equals("<bin_ili_izraz>") && i.djeca.size()==1) {
					Cvor bxili = bili.djeca.get(0);
					if(bxili.znak.equals("<bin_xili_izraz>") && bili.djeca.size()==1) {
						Cvor bi = bxili.djeca.get(0);
						if(bi.znak.equals("<bin_i_izraz>") && bxili.djeca.size()==1) {
							Cvor jed = bi.djeca.get(0);
							if(jed.znak.equals("<jednakosni_izraz>") && bi.djeca.size()==1) {
								Cvor odn = jed.djeca.get(0);
								if(odn.znak.equals("<odnosni_izraz>") && jed.djeca.size()==1) {
									Cvor ad = odn.djeca.get(0);
									if(ad.znak.equals("<aditivni_izraz>") && odn.djeca.size()==1) {
										Cvor mul = ad.djeca.get(0);
										if(mul.znak.equals("<multiplikativni_izraz>") && ad.djeca.size()==1) {
											Cvor cast = mul.djeca.get(0);
											if(cast.znak.equals("<cast_izraz>") && mul.djeca.size()==1) {
												Cvor un = cast.djeca.get(0);
												if(un.znak.equals("<unarni_izraz>") && cast.djeca.size()==1) {
													Cvor post = un.djeca.get(0);
													if(post.znak.equals("<postfiks_izraz>") && un.djeca.size()==1) {
														Cvor prim = post.djeca.get(0);
														if(prim.znak.equals("<primarni_izraz>") && post.djeca.size()==1) {
															Cvor niz = prim.djeca.get(0);
															if(niz.znak.equals("NIZ_ZNAKOVA") && prim.djeca.size()==1) {
																return niz;
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	private static boolean unutarPetlje(Cvor cvor) {
		if(cvor.roditelj==null) return false;
		if(cvor.roditelj.znak.equals("<naredba_petlje>")) return true;
		return unutarPetlje(cvor.roditelj);
	}

	private static boolean unutarVoidReturnFunkcije(Cvor cvor) {
		if(cvor.roditelj==null) return false;
		if(cvor.roditelj.znak.equals("<definicija_funkcije>")) {
			String znak = cvor.roditelj.djeca.get(1).svojstva.get("lJedinka");
			String tip = cvor.tablicaZnakova.getTip(znak);
			String povratniTip = tip.contains("->") ? tip.split("->")[1] : tip;
			if(povratniTip.equals("void")) return true;
			return false;
		}
		return unutarVoidReturnFunkcije(cvor.roditelj);
	}

	private static String povVrijednostFunkcije(Cvor cvor) {
		if(cvor.roditelj==null) return null;
		if(cvor.roditelj.znak.equals("<definicija_funkcije>")) {
			String znak = cvor.roditelj.djeca.get(1).svojstva.get("lJedinka");
			String tip = cvor.tablicaZnakova.getTip(znak);
			String povratniTip = tip.contains("->") ? tip.split("->")[1] : null;
			return povratniTip;
		}
		return povVrijednostFunkcije(cvor.roditelj);
	}

	private static boolean primarniIzraz(Cvor primarniIzraz) {
		if(primarniIzraz.djeca.size()==1) {
			if(primarniIzraz.djeca.get(0).znak.equals("IDN")) {

				if(primarniIzraz.tablicaZnakova.exists(primarniIzraz.djeca.get(0).svojstva.get("lJedinka"))==false) {
					System.out.println("<primarni_izraz> ::= IDN"+ispisZavrsnog(primarniIzraz.djeca.get(0)));
					return false;
				}
				primarniIzraz.svojstva.put("l-izraz", ""+primarniIzraz.tablicaZnakova.getLizraz(primarniIzraz.djeca.get(0).svojstva.get("lJedinka")));
				primarniIzraz.svojstva.put("tip", primarniIzraz.tablicaZnakova.getTip(primarniIzraz.djeca.get(0).svojstva.get("lJedinka")));
				return true;
			}else if(primarniIzraz.djeca.get(0).znak.equals("BROJ")) {
				Cvor broj = primarniIzraz.djeca.get(0);
				if(broj.svojstva.get("lJedinka").length()>11){
					System.out.println("<primarni_izraz> ::= BROJ"+ispisZavrsnog(broj));
					return false;
				}else{
					Long v = Long.parseLong(broj.svojstva.get("lJedinka"));

					if(v<-2147483648 || v > 2147483647){
						System.out.println("<primarni_izraz> ::= BROJ"+ispisZavrsnog(broj));
						return false;
					}
				}

				primarniIzraz.svojstva.put("tip","int");
				primarniIzraz.svojstva.put("l-izraz","false");
				return true;
			}else if(primarniIzraz.djeca.get(0).znak.equals("ZNAK")) {

				String znak = primarniIzraz.djeca.get(0).svojstva.get("lJedinka");
				if(znak.length()>3){
					if(znak.equals("'\\t'") || znak.equals("'\\n'") || znak.equals("'\\0'") || znak.equals("'\\''") || znak.equals("'\\\"'") || znak.equals("'\\\\'")){

					}else{
						System.out.println("<primarni_izraz> ::= ZNAK"+ispisZavrsnog(primarniIzraz.djeca.get(0)));
						return false;
					}
				}

				primarniIzraz.svojstva.put("tip", "char");
				primarniIzraz.svojstva.put("l-izraz", "false");
				return true;
			}else if(primarniIzraz.djeca.get(0).znak.equals("NIZ_ZNAKOVA")) {

				String niz = primarniIzraz.djeca.get(0).svojstva.get("lJedinka");
				for(int i=1;i<niz.length()-1; i++){
					if(niz.charAt(i)=='\\' && (niz.charAt(i+1)!='t' || niz.charAt(i+1)!='n' || niz.charAt(i+1)!='0' || niz.charAt(i+1)!='\'' || niz.charAt(i+1)!='\"' || niz.charAt(i+1)!='\\')){
						System.out.println("<primarni_izraz> ::= NIZ_ZNAKOVA"+ispisZavrsnog(primarniIzraz.djeca.get(0)));
						return false;
					}
					if(niz.charAt(i+1)!='\\'){
						i++;
					}
				}


				primarniIzraz.svojstva.put("tip", "niz(const(char))");
				primarniIzraz.svojstva.put("l-izraz", "false");
				return true;
			}
		}else if(primarniIzraz.djeca.size()==3) {
			if(provjeriProdukciju(primarniIzraz,"L_ZAGRADA","<izraz>","D_ZAGRADA")) {
				Cvor izraz = primarniIzraz.djeca.get(1);
				if(!izraz(izraz))
					return false;

				primarniIzraz.svojstva.put("tip",izraz.svojstva.get("tip"));
				primarniIzraz.svojstva.put("l-izraz",izraz.svojstva.get("l-izraz"));
				return true;
			}
		}
		return false;
	}

	private static boolean postfiksIzraz(Cvor postfiksIzraz) {
		if(provjeriProdukciju(postfiksIzraz,"<primarni_izraz>")) {

			if(primarniIzraz(postfiksIzraz.djeca.get(0))) {
				postfiksIzraz.svojstva.put("tip", postfiksIzraz.djeca.get(0).svojstva.get("tip"));
				postfiksIzraz.svojstva.put("l-izraz", ""+postfiksIzraz.djeca.get(0).svojstva.get("l-izraz"));
				return true;
			}else {
				return false;
			}
		}else if(provjeriProdukciju(postfiksIzraz,"<postfiks_izraz>","L_UGL_ZAGRADA","<izraz>","D_UGL_ZAGRADA")) {
			if(!postfiksIzraz(postfiksIzraz.djeca.get(0))) return false;

			if(!izraz(postfiksIzraz.djeca.get(2))) return false;


			if(
					(postfiksIzraz.djeca.get(0).svojstva.get("tip").equals("niz(int)")
							|| postfiksIzraz.djeca.get(0).svojstva.get("tip").equals("niz(char)")
							|| postfiksIzraz.djeca.get(0).svojstva.get("tip").equals("niz(const(int))")
							|| postfiksIzraz.djeca.get(0).svojstva.get("tip").equals("niz(const(char)"))

							&& (postfiksIzraz.djeca.get(2).svojstva.get("tip").equals("int")
							|| postfiksIzraz.djeca.get(2).svojstva.get("tip").equals("char")
							|| postfiksIzraz.djeca.get(2).svojstva.get("tip").equals("const(char)")
							|| postfiksIzraz.djeca.get(2).svojstva.get("tip").equals("const(int)"))){
				String currTip="";
				if(postfiksIzraz.djeca.get(0).svojstva.get("tip").equals("niz(int)")) {
					currTip="int";
				}else if(postfiksIzraz.djeca.get(0).svojstva.get("tip").equals("niz(char)")) {
					currTip="char";
				}else if((postfiksIzraz.djeca.get(0).svojstva.get("tip").equals("niz(const(int))"))) {
					currTip="const(int)";
				}else if((postfiksIzraz.djeca.get(0).svojstva.get("tip").equals("niz(const(char)"))) {
					currTip="const(char)";
				}
				postfiksIzraz.svojstva.put("tip", currTip);
				if(currTip.equals("const(char)") || currTip.equals("const(int)")) {
					postfiksIzraz.svojstva.put("l-izraz", "false");
				}else {
					postfiksIzraz.svojstva.put("l-izraz", "true");
				}
				return true;
			}else {
				if(postfiksIzraz.djeca.get(0).svojstva.get("tip")==null || postfiksIzraz.djeca.get(2).svojstva.get("tip")==null) {
					return false;
				}
				if(!postfiksIzraz.djeca.get(0).svojstva.get("tip").contains("niz") || postfiksIzraz.djeca.get(2).svojstva.get("tip").contains("niz")) {
					System.out.println("<postfiks_izraz> ::= <postfiks_izraz> L_UGL_ZAGRADA" +ispisZavrsnog(postfiksIzraz.djeca.get(1))+" <izraz> D_UGL_ZAGRADA"+ispisZavrsnog(postfiksIzraz.djeca.get(3)));
				}
				return false;
			}
		}else if(provjeriProdukciju(postfiksIzraz,"<postfiks_izraz>","L_ZAGRADA","D_ZAGRADA")) {
			if(postfiksIzraz(postfiksIzraz.djeca.get(0)) && postfiksIzraz.djeca.get(0).svojstva.get("tip").split("->")[0].equals("void")) {
				postfiksIzraz.svojstva.put("l-izraz", "false");
				postfiksIzraz.svojstva.put("tip", postfiksIzraz.djeca.get(0).svojstva.get("tip").split("->")[1]);
				return true;
			}
			if(postfiksIzraz.djeca.get(0).svojstva.get("tip")==null) {
				return false;
			}
			if(!postfiksIzraz.djeca.get(0).svojstva.get("tip").split("->")[0].equals("void")) {
				System.out.println("<postfiks_izraz> ::= <postfiks_izraz> L_ZAGRADA"+ ispisZavrsnog(postfiksIzraz.djeca.get(1))+" D_ZAGRADA"+ispisZavrsnog(postfiksIzraz.djeca.get(2)));
			}
			return false;
		}else if(provjeriProdukciju(postfiksIzraz,"<postfiks_izraz>","OP_INC") || provjeriProdukciju(postfiksIzraz,"<postfiks_izraz>","OP_DEC")) {
			if(postfiksIzraz(postfiksIzraz.djeca.get(0)) && postfiksIzraz.djeca.get(0).svojstva.get("l-izraz").equals("true") && !postfiksIzraz.djeca.get(0).svojstva.get("tip").contains("niz")) {
				postfiksIzraz.svojstva.put("tip", "int");
				postfiksIzraz.svojstva.put("l-izraz", "false");
				return true;
			}
			return false;
		}else if(provjeriProdukciju(postfiksIzraz,"<postfiks_izraz>","L_ZAGRADA","<lista_argumenata>","D_ZAGRADA")) {
			Cvor postfiksIzraz2 = postfiksIzraz.djeca.get(0);
			Cvor lZagrada = postfiksIzraz.djeca.get(1);
			Cvor listaArgumanta = postfiksIzraz.djeca.get(2);
			Cvor dZagrada = postfiksIzraz.djeca.get(3);

			if(!postfiksIzraz(postfiksIzraz2)) return false;

			if(!listaArgumenata(listaArgumanta)) return  false;

			if(!postfiksIzraz2.svojstva.get("tip").contains("->")){
				System.out.println("<postfiks_izraz> ::= <postfiks_izraz> L_ZAGRADA" + ispisZavrsnog(lZagrada) + " <lista_argumenata> D_ZAGRADA"+ ispisZavrsnog(dZagrada));
				return false;
			}
			String pov = postfiksIzraz2.svojstva.get("tip").split("->")[1];
			String params = postfiksIzraz2.svojstva.get("tip").split("->")[0];
			params = params.contains("]") ? params.substring(1,params.length()-1) : params;



			String argTip = listaArgumanta.svojstva.get("tipovi");
			argTip = argTip.contains("]") ? argTip.substring(1,argTip.length()-1) : argTip;

			String[] paramsAr = params.split(",");
			String[] argTipAr = argTip.split(",");

			if(paramsAr.length!=argTipAr.length){
				System.out.println("<postfiks_izraz> ::= <postfiks_izraz> L_ZAGRADA" + ispisZavrsnog(lZagrada) + " <lista_argumenata> D_ZAGRADA"+ ispisZavrsnog(dZagrada));
				return false;
			}

			for(int i=0;i<paramsAr.length;i++){
				if(!castU(argTipAr[i],paramsAr[i])){
					System.out.println("<postfiks_izraz> ::= <postfiks_izraz> L_ZAGRADA" + ispisZavrsnog(lZagrada) + " <lista_argumenata> D_ZAGRADA"+ ispisZavrsnog(dZagrada));
					return false;
				}
			}

			postfiksIzraz.svojstva.put("tip", pov);
			postfiksIzraz.svojstva.put("l-izraz", "false");

			return true;
		}
		return false;
	}


	private static boolean listaArgumenata(Cvor listaArgumenata) {
		if(provjeriProdukciju(listaArgumenata,"<izraz_pridruzivanja>")) {
			if(izrazPridruzivanja(listaArgumenata.djeca.get(0))) {
				listaArgumenata.svojstva.put("tipovi", listaArgumenata.djeca.get(0).svojstva.get("tip"));
				return true;
			}

			return false;
		}else if(provjeriProdukciju(listaArgumenata,"<lista_argumenata>","ZAREZ","<izraz_pridruzivanja>")) {


			if(listaArgumenata(listaArgumenata.djeca.get(0)) && izrazPridruzivanja(listaArgumenata.djeca.get(2))) {
				listaArgumenata.svojstva.put("tipovi", listaArgumenata.djeca.get(0).svojstva.get("tipovi")+", "+listaArgumenata.djeca.get(2).svojstva.get("tip"));
				return true;
			}
		}else {
			return false;
		}
		return false;
	}

	private static boolean unarniIzraz(Cvor unarniIzraz) {
		if(provjeriProdukciju(unarniIzraz,"<postfiks_izraz>")) {
			if(postfiksIzraz(unarniIzraz.djeca.get(0))) {
				unarniIzraz.svojstva.put("tip", unarniIzraz.djeca.get(0).svojstva.get("tip"));
				unarniIzraz.svojstva.put("l-izraz",unarniIzraz.djeca.get(0).svojstva.get("l-izraz"));
				return true;
			}
			return false;
		}else if(provjeriProdukciju(unarniIzraz,"OP_INC","<unarni_izraz>") || provjeriProdukciju(unarniIzraz,"OP_DEC","<unarni_izraz>")) {
			if(unarniIzraz(unarniIzraz.djeca.get(1)) && unarniIzraz.djeca.get(1).svojstva.get("l-izraz").equals("true") && !unarniIzraz.djeca.get(1).svojstva.get("tip").contains("niz")) {
				unarniIzraz.svojstva.put("tip", "int");
				unarniIzraz.svojstva.put("l-izraz", "false");
				return true;
			}

			if(unarniIzraz.djeca.get(1).svojstva.get("tip")==null) {
				return false;
			}

			if(unarniIzraz.djeca.get(1).svojstva.get("tip").contains("niz") && provjeriProdukciju(unarniIzraz,"OP_INC","<unarni_izraz>")) {
				System.out.println("<postfiks_izraz> ::= OP_INC"+ispisZavrsnog(unarniIzraz.djeca.get(0))+" <unarni_izraz>");
			}

			if(unarniIzraz.djeca.get(1).svojstva.get("tip").contains("niz") && provjeriProdukciju(unarniIzraz,"OP_DEC","<unarni_izraz>")) {
				System.out.println("<postfiks_izraz> ::= OP_DEC"+ispisZavrsnog(unarniIzraz.djeca.get(0))+" <unarni_izraz>");
			}

			return false;
		}else if(provjeriProdukciju(unarniIzraz,"<unarni_operator>","<cast_izraz>")) {


			if(!castIzraz(unarniIzraz.djeca.get(1))) return false;



			if(castIzraz(unarniIzraz.djeca.get(1)) && !unarniIzraz.djeca.get(1).svojstva.get("tip").contains("niz")) {
				unarniIzraz.svojstva.put("tip", "int");
				unarniIzraz.svojstva.put("l-izraz","false");
				return true;
			}
			if(unarniIzraz.djeca.get(1).svojstva.get("tip")!=null) {
				if(unarniIzraz.djeca.get(1).svojstva.get("tip").contains("niz")) {
					System.out.print("<unarni_izraz> ::= <unarni_operator> <cast_izraz>");
				}
			}
			return false;
		}else {
			return false;
		}
	}

	private static boolean castIzraz(Cvor castIzraz) {
		if(provjeriProdukciju(castIzraz,"<unarni_izraz>")) {
			if(unarniIzraz(castIzraz.djeca.get(0))) {
				castIzraz.svojstva.put("tip", castIzraz.djeca.get(0).svojstva.get("tip"));
				castIzraz.svojstva.put("l-izraz",castIzraz.djeca.get(0).svojstva.get("l-izraz"));
				return true;
			}
			return false;
		}else if(provjeriProdukciju(castIzraz,"L_ZAGRADA","<ime_tipa>","D_ZAGRADA","<cast_izraz>")) {
			if(imeTipa(castIzraz.djeca.get(1)) && castIzraz(castIzraz.djeca.get(3))) {
				Cvor imeTipa = castIzraz.djeca.get(1);
				Cvor castIzraz2 = castIzraz.djeca.get(3);

				if(!castU(castIzraz2.svojstva.get("tip"),imeTipa.svojstva.get("tip"))
						&& !(castU(castIzraz2.svojstva.get("tip"),"int") && castU(imeTipa.svojstva.get("tip"),"char"))){
					System.out.println("<cast_izraz> ::= L_ZAGRADA"+ispisZavrsnog(castIzraz.djeca.get(0))+" <ime_tipa> D_ZAGRADA"+ispisZavrsnog(castIzraz.djeca.get(2))+" <cast_izraz>");
					return false;
				}

				castIzraz.svojstva.put("tip", imeTipa.svojstva.get("tip"));
				castIzraz.svojstva.put("l-izraz", "false");
				return true;

				/*
				//ako je int se moze samo u int/const int
				if(castIzraz.djeca.get(3).svojstva.get("tip").equals("int") || castIzraz.djeca.get(3).svojstva.get("tip").equals("const(int)")) {
					if(castIzraz.djeca.get(1).svojstva.get("tip").equals("int") || castIzraz.djeca.get(1).svojstva.get("tip").equals("const(int)")) {

						castIzraz.svojstva.put("tip", castIzraz.djeca.get(1).svojstva.get("tip"));
						castIzraz.svojstva.put("l-izraz", "false");
						return true;
					}
					System.out.println("<cast_izraz> ::= L_ZAGRADA"+ispisZavrsnog(castIzraz.djeca.get(0))+" <ime_tipa> D_ZAGRADA"+ispisZavrsnog(castIzraz.djeca.get(2))+" <cast_izraz>");
					return false;
				}

				//ako je char se moze castati u bilo kaj kaj nije niz
				if(castIzraz.djeca.get(3).svojstva.get("tip").equals("char") || castIzraz.djeca.get(3).svojstva.get("tip").equals("const(char)")) {
					if(castIzraz.djeca.get(1).svojstva.get("tip").contains("niz")) {
						System.out.println("<cast_izraz> ::= L_ZAGRADA"+ispisZavrsnog(castIzraz.djeca.get(0))+" <ime_tipa> D_ZAGRADA"+ispisZavrsnog(castIzraz.djeca.get(2))+" <cast_izraz>");
						return false;
					}
					castIzraz.svojstva.put("tip", castIzraz.djeca.get(1).svojstva.get("tip"));
					castIzraz.svojstva.put("l-izraz", "false");
					return true;

				}

				//provjeri int nizove
				if(castIzraz.djeca.get(3).svojstva.get("tip").equals("niz(const(int))") || castIzraz.djeca.get(3).svojstva.get("tip").equals("niz(int)")) {
					if(castIzraz.djeca.get(1).svojstva.get("tip").equals("niz(const(int))") || castIzraz.djeca.get(1).svojstva.get("tip").equals("niz(int)")) {
						castIzraz.svojstva.put("tip", castIzraz.djeca.get(1).svojstva.get("tip"));
						castIzraz.svojstva.put("l-izraz", "false");
						return true;
					}
					System.out.println("<cast_izraz> ::= L_ZAGRADA"+ispisZavrsnog(castIzraz.djeca.get(0))+" <ime_tipa> D_ZAGRADA"+ispisZavrsnog(castIzraz.djeca.get(2))+" <cast_izraz>");
					return false;
				}
				//provjeri char nizove
				if(castIzraz.djeca.get(3).svojstva.get("tip").equals("niz(const(char))") || castIzraz.djeca.get(3).svojstva.get("tip").equals("niz(char)")) {
					if(castIzraz.djeca.get(1).svojstva.get("tip").equals("niz(const(char))") || castIzraz.djeca.get(1).svojstva.get("tip").equals("niz(char)")) {
						castIzraz.svojstva.put("tip", castIzraz.djeca.get(1).svojstva.get("tip"));
						castIzraz.svojstva.put("l-izraz", "false");
						return true;
					}
					System.out.println("<cast_izraz> ::= L_ZAGRADA"+ispisZavrsnog(castIzraz.djeca.get(0))+" <ime_tipa> D_ZAGRADA"+ispisZavrsnog(castIzraz.djeca.get(2))+" <cast_izraz>");
					return false;
				}*/
			}else {
				return false;
			}
		}
		return false;
	}

	private static boolean imeTipa(Cvor imeTipa) {
		if(provjeriProdukciju(imeTipa,"<specifikator_tipa>")) {
			if(specifikatorTipa(imeTipa.djeca.get(0))) {
				imeTipa.svojstva.put("tip", imeTipa.djeca.get(0).svojstva.get("tip"));
				return true;
			}
			return false;
		}else if(provjeriProdukciju(imeTipa,"KR_CONST","<specifikator_tipa>")) {
			if(specifikatorTipa(imeTipa.djeca.get(1)) && !imeTipa.djeca.get(1).svojstva.get("tip").equals("void")) {
				imeTipa.svojstva.put("tip", "const("+imeTipa.djeca.get(1).svojstva.get("tip")+")");
				return true;
			}
			if(imeTipa.djeca.get(1).svojstva.get("tip")==null) {return false;}
			if(imeTipa.djeca.get(1).svojstva.get("tip").equals("void")) {
				System.out.println("<specifikator_tipa> ::= KR_CONST"+ispisZavrsnog(imeTipa.djeca.get(0))+" <specifikator_tipa>");
			}
			return false;
		}else {
			return false;
		}
	}

	private static boolean specifikatorTipa(Cvor specifikatorTipa) {
		if(provjeriProdukciju(specifikatorTipa,"KR_VOID")) {
			specifikatorTipa.svojstva.put("tip", "void");
			return true;
		}else if(provjeriProdukciju(specifikatorTipa,"KR_CHAR")) {
			specifikatorTipa.svojstva.put("tip", "char");
			return true;
		}else if(provjeriProdukciju(specifikatorTipa,"KR_INT")) {
			specifikatorTipa.svojstva.put("tip","int");
			return true;
		}
		return false;
	}

	private static boolean multiplikativniIzraz(Cvor multiplikativniIzraz) {
		if(provjeriProdukciju(multiplikativniIzraz,"<cast_izraz>")) {
			if(castIzraz(multiplikativniIzraz.djeca.get(0))) {
				multiplikativniIzraz.svojstva.put("tip",multiplikativniIzraz.djeca.get(0).svojstva.get("tip"));
				multiplikativniIzraz.svojstva.put("l-izraz",multiplikativniIzraz.djeca.get(0).svojstva.get("l-izraz"));
				return true;
			}
			return false;
		}else if(provjeriProdukciju(multiplikativniIzraz,"<multiplikativni_izraz>","OP_PUTA","<cast_izraz>") || provjeriProdukciju(multiplikativniIzraz,"<multiplikativni_izraz>","OP_DIJELI","<cast_izraz>") || provjeriProdukciju(multiplikativniIzraz,"<multiplikativni_izraz>","OP_MOD","<cast_izraz>")) {
			if(!multiplikativniIzraz(multiplikativniIzraz.djeca.get(0))) return false;
			if(!castIzraz(multiplikativniIzraz.djeca.get(2))) return false;

			if(!multiplikativniIzraz.djeca.get(0).svojstva.get("tip").contains("niz") && !multiplikativniIzraz.djeca.get(2).svojstva.get("tip").contains("niz")) {
				multiplikativniIzraz.svojstva.put("tip", "int");
				multiplikativniIzraz.svojstva.put("l-izraz","false");
				return true;
			}
			if(multiplikativniIzraz.djeca.get(0).svojstva.get("tip")==null || multiplikativniIzraz.djeca.get(2).svojstva.get("tip")==null) {
				return false;
			}
			if(multiplikativniIzraz.djeca.get(0).svojstva.get("tip").contains("niz") || multiplikativniIzraz.djeca.get(2).svojstva.get("tip").contains("niz")) {
				System.out.println("<multiplikativni_izraz> ::= <multiplikativni_izraz> "+multiplikativniIzraz.djeca.get(2).znak+ispisZavrsnog(multiplikativniIzraz.djeca.get(2))+" <cast_izraz>");
				return false;
			}
		}
		return false;
	}

	private static boolean aditivniIzraz(Cvor aditivniIzraz) {
		if(provjeriProdukciju(aditivniIzraz,"<multiplikativni_izraz>")) {
			if(multiplikativniIzraz(aditivniIzraz.djeca.get(0))) {
				aditivniIzraz.svojstva.put("tip",aditivniIzraz.djeca.get(0).svojstva.get("tip"));
				aditivniIzraz.svojstva.put("l-izraz",aditivniIzraz.djeca.get(0).svojstva.get("l-izraz"));
				return true;
			}
		}else if(provjeriProdukciju(aditivniIzraz,"<aditivni_izraz>","PLUS","<multiplikativni_izraz>") || provjeriProdukciju(aditivniIzraz,"<aditivni_izraz>","MINUS","<multiplikativni_izraz>")) {
			if(!aditivniIzraz(aditivniIzraz.djeca.get(0))) return false;
			if(!multiplikativniIzraz(aditivniIzraz.djeca.get(2))) return false;

			if( !aditivniIzraz.djeca.get(0).svojstva.get("tip").contains("niz") && !aditivniIzraz.djeca.get(2).svojstva.get("tip").contains("niz")) {
				aditivniIzraz.svojstva.put("tip","int");
				aditivniIzraz.svojstva.put("l-izraz","false");
				return true;
			}
			if(aditivniIzraz.djeca.get(0).svojstva.get("tip")==null || aditivniIzraz.djeca.get(2).svojstva.get("tip")==null) {
				return false;
			}
			if(aditivniIzraz.djeca.get(0).svojstva.get("tip").contains("niz") || aditivniIzraz.djeca.get(2).svojstva.get("tip").contains("niz")) {
				System.out.println("<aditivni_izraz> ::= <aditivni_izraz> "+aditivniIzraz.djeca.get(1).znak+ispisZavrsnog(aditivniIzraz.djeca.get(1))+" <multiplikativni_izraz>");
				return false;
			}
		}
		return false;
	}

	private static boolean odnosniIzraz(Cvor odnosniIzraz) {
		if(provjeriProdukciju(odnosniIzraz,"<aditivni_izraz>")) {
			if(aditivniIzraz(odnosniIzraz.djeca.get(0))) {
				odnosniIzraz.svojstva.put("tip",odnosniIzraz.djeca.get(0).svojstva.get("tip"));
				odnosniIzraz.svojstva.put("l-izraz",odnosniIzraz.djeca.get(0).svojstva.get("l-izraz"));
				return true;
			}
		}else if(provjeriProdukciju(odnosniIzraz,"<odnosni_izraz>","OP_LT","<aditivni_izraz>") || provjeriProdukciju(odnosniIzraz,"<odnosni_izraz>","OP_GT","<aditivni_izraz>") || provjeriProdukciju(odnosniIzraz,"<odnosni_izraz>","OP_LTE","<aditivni_izraz>") || provjeriProdukciju(odnosniIzraz,"<odnosni_izraz>","OP_GTE","<aditivni_izraz>")) {
			if(!odnosniIzraz(odnosniIzraz.djeca.get(0))) return false;
			if(!aditivniIzraz(odnosniIzraz.djeca.get(2))) return false;
			if(!odnosniIzraz.djeca.get(0).svojstva.get("tip").contains("niz") && !odnosniIzraz.djeca.get(2).svojstva.get("tip").contains("niz")) {
				odnosniIzraz.svojstva.put("tip","int");
				odnosniIzraz.svojstva.put("l-izraz","false");
				return true;
			}
			if(odnosniIzraz.djeca.get(0).svojstva.get("tip")==null || odnosniIzraz.djeca.get(2).svojstva.get("tip")==null) {
				return false;
			}
			if(odnosniIzraz.djeca.get(0).svojstva.get("tip").contains("niz") || odnosniIzraz.djeca.get(2).svojstva.get("tip").contains("niz")) {
				System.out.println("<odnosni_izraz> ::= <odnosni_izraz> "+odnosniIzraz.djeca.get(1).znak+ispisZavrsnog(odnosniIzraz.djeca.get(1))+" <aditivni_izraz>");
				return false;
			}
		}
		return false;
	}

	private static boolean jednakosniIzraz(Cvor jednakosniIzraz) {
		if(provjeriProdukciju(jednakosniIzraz,"<odnosni_izraz>")) {
			if(odnosniIzraz(jednakosniIzraz.djeca.get(0))) {
				jednakosniIzraz.svojstva.put("tip",jednakosniIzraz.djeca.get(0).svojstva.get("tip"));
				jednakosniIzraz.svojstva.put("l-izraz",jednakosniIzraz.djeca.get(0).svojstva.get("l-izraz"));
				return true;
			}
		}else if(provjeriProdukciju(jednakosniIzraz,"<jednakosni_izraz>","OP_EQ","<odnosni_izraz>") || provjeriProdukciju(jednakosniIzraz,"<jednakosni_izraz>","OP_NEQ","<odnosni_izraz>")) {
			Cvor jednakosniIzraz2 = jednakosniIzraz.djeca.get(0);
			Cvor odnosniIzraz = jednakosniIzraz.djeca.get(2);
			if(!jednakosniIzraz(jednakosniIzraz2)) return false;
			if(!castU(jednakosniIzraz2.svojstva.get("tip"),"int")) {
				System.out.println("<jednakosni_izraz> ::= <jednakosni_izraz> "+jednakosniIzraz.djeca.get(1).znak+ispisZavrsnog(jednakosniIzraz.djeca.get(1))+" <odnosni_izraz>");
				return false;
			}
			if(!odnosniIzraz(odnosniIzraz)) return false;
			if(!castU(odnosniIzraz.svojstva.get("tip"),"int")) {
				System.out.println("<jednakosni_izraz> ::= <jednakosni_izraz> "+jednakosniIzraz.djeca.get(1).znak+ispisZavrsnog(jednakosniIzraz.djeca.get(1))+" <odnosni_izraz>");
				return false;
			}
			jednakosniIzraz.svojstva.put("tip", "int");
			jednakosniIzraz.svojstva.put("l-izraz", "false");

			return true;
		}
		return false;
	}

	private static boolean binIzraz(Cvor binIzraz) {
		if(provjeriProdukciju(binIzraz,"<jednakosni_izraz>")) {
			if(jednakosniIzraz(binIzraz.djeca.get(0))) {
				binIzraz.svojstva.put("tip",binIzraz.djeca.get(0).svojstva.get("tip"));
				binIzraz.svojstva.put("l-izraz",binIzraz.djeca.get(0).svojstva.get("l-izraz"));
				return true;
			}
		}else if(provjeriProdukciju(binIzraz,"<bin_i_izraz>","OP_BIN_I","<jednakosni_izraz>")) {
			if(!binIzraz(binIzraz.djeca.get(0))) return false;
			if(!jednakosniIzraz(binIzraz.djeca.get(2))) return false;

			if(!binIzraz.djeca.get(0).svojstva.get("tip").contains("niz") &&  !binIzraz.djeca.get(2).svojstva.get("tip").contains("niz")) {
				binIzraz.svojstva.put("tip", "int");
				binIzraz.svojstva.put("l-izraz", "false");
				return true;
			}
			if(binIzraz.djeca.get(0).svojstva.get("tip")==null || binIzraz.djeca.get(2).svojstva.get("tip")==null) {
				return false;
			}
			if(binIzraz.djeca.get(0).svojstva.get("tip").contains("niz") || binIzraz.djeca.get(2).svojstva.get("tip").contains("niz")) {
				System.out.println("<bin_i_izraz> ::= <bin_i_izraz> "+binIzraz.djeca.get(1).znak+ispisZavrsnog(binIzraz.djeca.get(1))+" <jednakosni_izraz>");
				return false;
			}
		}
		return false;
	}

	private static boolean binXiliIzraz(Cvor binXiliIzraz) {
		if(provjeriProdukciju(binXiliIzraz,"<bin_i_izraz>")) {
			if(binIzraz(binXiliIzraz.djeca.get(0))) {
				binXiliIzraz.svojstva.put("tip", binXiliIzraz.djeca.get(0).svojstva.get("tip"));
				binXiliIzraz.svojstva.put("l-izraz",binXiliIzraz.djeca.get(0).svojstva.get("l-izraz"));
				return true;
			}
		}else if(provjeriProdukciju(binXiliIzraz,"<bin_xili_izraz>","OP_BIN_XILI","<bin_i_izraz>")) {
			if(!binXiliIzraz(binXiliIzraz.djeca.get(0))) return false;
			if(!binIzraz(binXiliIzraz.djeca.get(2))) return false;

			if( !binXiliIzraz.djeca.get(0).svojstva.get("tip").contains("niz") && !binXiliIzraz.djeca.get(2).svojstva.get("tip").contains("niz")) {
				binXiliIzraz.svojstva.put("tip","int");
				binXiliIzraz.svojstva.put("l-izraz","false");
				return true;
			}
			if(binXiliIzraz.djeca.get(0).svojstva.get("tip")==null || binXiliIzraz.djeca.get(2).svojstva.get("tip")==null) {
				return false;
			}
			if(binXiliIzraz.djeca.get(0).svojstva.get("tip").contains("niz") || binXiliIzraz.djeca.get(2).svojstva.get("tip").contains("niz")) {
				System.out.println("<bin_xili_izraz> ::= <bin_xili_izraz> "+binXiliIzraz.djeca.get(1).znak+ispisZavrsnog(binXiliIzraz.djeca.get(1))+" <bin_i_izraz>");
				return false;
			}
		}
		return false;
	}

	private static boolean binIliIzraz(Cvor binIliIzraz) {
		if(provjeriProdukciju(binIliIzraz,"<bin_xili_izraz>")) {
			if(binXiliIzraz(binIliIzraz.djeca.get(0))) {
				binIliIzraz.svojstva.put("tip",binIliIzraz.djeca.get(0).svojstva.get("tip"));
				binIliIzraz.svojstva.put("l-izraz",binIliIzraz.djeca.get(0).svojstva.get("l-izraz"));
				return true;
			}
		}else if(provjeriProdukciju(binIliIzraz,"<bin_ili_izraz>","OP_BIN_ILI","<bin_xili_izraz>")) {
			if(!binIliIzraz(binIliIzraz.djeca.get(0))) return false;
			if(! binXiliIzraz(binIliIzraz.djeca.get(2))) return false;

			if( !binIliIzraz.djeca.get(0).svojstva.get("tip").contains("niz") && !binIliIzraz.djeca.get(2).svojstva.get("tip").contains("niz")) {
				binIliIzraz.svojstva.put("tip","int");
				binIliIzraz.svojstva.put("l-izraz","false");
				return true;
			}
			if(binIliIzraz.djeca.get(0).svojstva.get("tip")==null || binIliIzraz.djeca.get(2).svojstva.get("tip")==null){
				return false;
			}
			if(binIliIzraz.djeca.get(0).svojstva.get("tip").contains("niz") || binIliIzraz.djeca.get(2).svojstva.get("tip").contains("niz")) {
				System.out.println("<bin_ili_izraz> ::= <bin_ili_izraz> "+binIliIzraz.djeca.get(1).znak+ispisZavrsnog(binIliIzraz.djeca.get(1))+" <bin_xili_izraz>");
				return false;
			}
		}
		return false;
	}

	private static boolean logIIzraz(Cvor logIIzraz) {
		if(provjeriProdukciju(logIIzraz,"<bin_ili_izraz>")) {
			if(binIliIzraz(logIIzraz.djeca.get(0))) {
				logIIzraz.svojstva.put("tip",logIIzraz.djeca.get(0).svojstva.get("tip"));
				logIIzraz.svojstva.put("l-izraz",logIIzraz.djeca.get(0).svojstva.get("l-izraz"));
				return true;
			}
		}else if(provjeriProdukciju(logIIzraz,"<log_i_izraz>","OP_I","<bin_ili_izraz>")) {
			if(!logIIzraz(logIIzraz.djeca.get(0)) ) return false;
			if(!binIliIzraz(logIIzraz.djeca.get(2))) return false;

			if(!logIIzraz.djeca.get(0).svojstva.get("tip").contains("niz") && !logIIzraz.djeca.get(2).svojstva.get("tip").contains("niz")) {
				logIIzraz.svojstva.put("tip", "int");
				logIIzraz.svojstva.put("l-izraz", "false");
				return true;
			}
			if(logIIzraz.djeca.get(0).svojstva.get("tip")==null || logIIzraz.djeca.get(2).svojstva.get("tip")==null){
				return false;
			}
			if(logIIzraz.djeca.get(0).svojstva.get("tip").contains("niz") || logIIzraz.djeca.get(2).svojstva.get("tip").contains("niz")) {
				System.out.println("<log_i_izraz> ::= <log_i_izraz> "+logIIzraz.djeca.get(1).znak+ispisZavrsnog(logIIzraz.djeca.get(1))+" <bin_ili_izraz>");
				return false;
			}
		}

		return false;
	}

	private static boolean logIliIzraz(Cvor logIliIzraz) {
		if(provjeriProdukciju(logIliIzraz,"<log_i_izraz>")) {
			if(logIIzraz(logIliIzraz.djeca.get(0))) {
				logIliIzraz.svojstva.put("tip",logIliIzraz.djeca.get(0).svojstva.get("tip"));
				logIliIzraz.svojstva.put("l-izraz", logIliIzraz.djeca.get(0).svojstva.get("l-izraz"));
				return true;
			}
		}else if(provjeriProdukciju(logIliIzraz,"<log_ili_izraz>","OP_ILI","<log_i_izraz>")) {
			if(!logIliIzraz(logIliIzraz.djeca.get(0)) ) return false;
			if(!logIIzraz(logIliIzraz.djeca.get(2))) return false;

			if(!logIliIzraz.djeca.get(0).svojstva.get("tip").contains("niz") &&  !logIliIzraz.djeca.get(2).svojstva.get("tip").contains("niz")) {
				logIliIzraz.svojstva.put("tip","int");
				logIliIzraz.svojstva.put("l-izraz","false");
				return true;
			}
			if(logIliIzraz.djeca.get(0).svojstva.get("tip")==null || logIliIzraz.djeca.get(2).svojstva.get("tip")==null){
				return false;
			}
			if(logIliIzraz.djeca.get(0).svojstva.get("tip").contains("niz") || logIliIzraz.djeca.get(2).svojstva.get("tip").contains("niz")) {
				System.out.println("<log_ili_izraz> ::= <log_ili_izraz> "+logIliIzraz.djeca.get(1).znak+ispisZavrsnog(logIliIzraz.djeca.get(1))+" <log_i_izraz>");
				return false;
			}
		}
		return false;
	}

	private static boolean izrazPridruzivanja(Cvor izrazPridruzivanja) {
		if(provjeriProdukciju(izrazPridruzivanja,"<log_ili_izraz>")) {
			if(logIliIzraz(izrazPridruzivanja.djeca.get(0))) {
				izrazPridruzivanja.svojstva.put("tip",izrazPridruzivanja.djeca.get(0).svojstva.get("tip"));
				izrazPridruzivanja.svojstva.put("l-izraz",izrazPridruzivanja.djeca.get(0).svojstva.get("l-izraz"));
				return true;
			}
		}else if(provjeriProdukciju(izrazPridruzivanja,"<postfiks_izraz>","OP_PRIDRUZI","<izraz_pridruzivanja>")) {
			if(!postfiksIzraz(izrazPridruzivanja.djeca.get(0)) ) return false;
			if(!izrazPridruzivanja(izrazPridruzivanja.djeca.get(2))) return false;

			if( izrazPridruzivanja.djeca.get(0).svojstva.get("l-izraz").equals("true")) {
				String tip1=izrazPridruzivanja.djeca.get(2).svojstva.get("tip"),tip2=izrazPridruzivanja.djeca.get(0).svojstva.get("tip");
				boolean isValid=false;

				if(tip1.equals(tip2)) {
					isValid=true;
				}
				if((tip1.equals("char") || tip1.equals("const(char)")) && !tip2.contains("niz")) {
					isValid=true;
				}
				if((tip1.equals("int") || tip1.equals("const(int)")) && (tip2.equals("int") || tip2.equals("const(int)"))) {
					isValid=true;
				}
				if((tip1.equals("niz(int") || tip1.equals("niz(const(int))")) && (tip2.equals("niz(const(int)") || tip2.equals("niz(int)"))) {
					isValid=true;
				}
				if((tip1.equals("niz(char") || tip1.equals("niz(const(char))")) && (tip2.equals("niz(const(char)") || tip2.equals("niz(char)"))) {
					isValid=true;
				}
				if(isValid) {
					izrazPridruzivanja.svojstva.put("tip",izrazPridruzivanja.djeca.get(0).svojstva.get("tip"));
					izrazPridruzivanja.svojstva.put("l-izraz", "false");
					return true;
				}
				System.out.println("<izraz_pridruzivanja> ::= <postfiks_izraz> OP_PRIDRUZI" + ispisZavrsnog(izrazPridruzivanja.djeca.get(1)) + "<izraz_pridruzivanja>");
				return false;
			}
			if(izrazPridruzivanja.djeca.get(0).svojstva.get("l-izraz")==null) {
				return false;
			}
			if(!izrazPridruzivanja.djeca.get(0).svojstva.get("l-izraz").equals("true")) {
				System.out.println("<izraz_pridruzivanja> ::= <postfiks_izraz> OP_PRIDRUZI"+ispisZavrsnog(izrazPridruzivanja.djeca.get(1))+" <izraz_pridruzivanja>");
			}
		}
		return false;
	}
	private static boolean izraz(Cvor izraz) {
		if(provjeriProdukciju(izraz,"<izraz_pridruzivanja>")) {
			if(izrazPridruzivanja(izraz.djeca.get(0))) {
				izraz.svojstva.put("tip",izraz.djeca.get(0).svojstva.get("tip"));
				izraz.svojstva.put("l-izraz",izraz.djeca.get(0).svojstva.get("l-izraz"));
				return true;
			}
		}else if(provjeriProdukciju(izraz,"<izraz>","ZAREZ","<izraz_pridruzivanja>")) {
			if(izraz(izraz.djeca.get(0)) && izrazPridruzivanja(izraz.djeca.get(2))) {
				izraz.svojstva.put("tip",izraz.djeca.get(2).svojstva.get("tip"));
				izraz.svojstva.put("l-izraz","false");
				return true;
			}
		}
		return false;
	}

	private static boolean slozenaNaredba(Cvor slozenaNaredba){
		if(provjeriProdukciju(slozenaNaredba, "L_VIT_ZAGRADA", "<lista_naredbi>", "D_VIT_ZAGRADA")) {
			Cvor listaNaredbi = slozenaNaredba.djeca.get(1);

			if(!listaNaredbi(listaNaredbi)) return false;

			return true;
		}

		if(provjeriProdukciju(slozenaNaredba, "L_VIT_ZAGRADA", "<lista_deklaracija>", "<lista_naredbi>", "D_VIT_ZAGRADA")) {
			Cvor listaDeklaracija = slozenaNaredba.djeca.get(1);
			Cvor listaNaredbi = slozenaNaredba.djeca.get(2);

			if(!listaDeklaracija(listaDeklaracija)) return false;

			if(!listaNaredbi(listaNaredbi)) return false;

			return true;
		}

		return false;
	}

	private static boolean listaNaredbi(Cvor listaNaredbi) {

		if(provjeriProdukciju(listaNaredbi, "<naredba>")) {
			return naredba(listaNaredbi.djeca.get(0));
		}

		if(provjeriProdukciju(listaNaredbi, "<lista_naredbi>", "<naredba>")) {
			Cvor listaNaredbi2 = listaNaredbi.djeca.get(0);
			Cvor naredba = listaNaredbi.djeca.get(1);

			if(!listaNaredbi(listaNaredbi2)) return false;

			if(!naredba(naredba)) return false;

			return true;
		}

		return false;
	}

	private static boolean naredba(Cvor naredba) {
		if(provjeriProdukciju(naredba, "<slozena_naredba>"))
			return slozenaNaredba(naredba.djeca.get(0));


		if(provjeriProdukciju(naredba, "<izraz_naredba>"))
			return izrazNaredba(naredba.djeca.get(0));


		if(provjeriProdukciju(naredba, "<naredba_grananja>"))
			return naredbaGrananja(naredba.djeca.get(0));


		if(provjeriProdukciju(naredba, "<naredba_petlje>"))
			return naredbaPetlje(naredba.djeca.get(0));


		if(provjeriProdukciju(naredba, "<naredba_skoka>"))
			return naredbaSkoka(naredba.djeca.get(0));


		return false;
	}

	private static boolean izrazNaredba(Cvor izrazNaredba) {
		if(provjeriProdukciju(izrazNaredba, "TOCKAZAREZ")) {
			izrazNaredba.svojstva.put("tip", "int");
			return true;
		}

		if(provjeriProdukciju(izrazNaredba, "<izraz>", "TOCKAZAREZ")) {
			Cvor izraz = izrazNaredba.djeca.get(0);

			if(!izraz(izraz)) return false;

			izrazNaredba.svojstva.put("tip", izraz.svojstva.get("tip"));

			return true;
		}
		return false;
	}

	private static boolean naredbaGrananja(Cvor naredbaGrananja) {

		if(provjeriProdukciju(naredbaGrananja, "KR_IF", "L_ZAGRADA", "<izraz>", "D_ZAGRADA", "<naredba>")) {
			Cvor krIf = naredbaGrananja.djeca.get(0);
			Cvor lZagrada = naredbaGrananja.djeca.get(1);
			Cvor izraz = naredbaGrananja.djeca.get(2);
			Cvor dZagrada = naredbaGrananja.djeca.get(3);
			Cvor naredba = naredbaGrananja.djeca.get(4);

			if(!izraz(izraz)) return false;

			if(!castU(izraz.svojstva.get("tip"),"int")) {
				System.out.println("<naredba_grananja> ::= KR_IF" + ispisZavrsnog(krIf) + " L_ZAGRADA" + ispisZavrsnog(lZagrada) + " <izraz> D_ZAGRADA" + ispisZavrsnog(dZagrada) + " <naredba>");
				return false;
			}

			if(!naredba(naredba)) return false;

			return true;

		}

		if(provjeriProdukciju(naredbaGrananja, "KR_IF", "L_ZAGRADA", "<izraz>", "D_ZAGRADA", "<naredba>", "KR_ELSE", "<naredba>")) {
			Cvor krIf = naredbaGrananja.djeca.get(0);
			Cvor lZagrada = naredbaGrananja.djeca.get(1);
			Cvor izraz = naredbaGrananja.djeca.get(2);
			Cvor dZagrada = naredbaGrananja.djeca.get(3);
			Cvor naredba = naredbaGrananja.djeca.get(4);
			Cvor krElse = naredbaGrananja.djeca.get(5);
			Cvor naredba2 = naredbaGrananja.djeca.get(6);

			if(!izraz(izraz)) return false;

			if(!castU(izraz.svojstva.get("tip"),"int")) {
				System.out.println("<naredba_grananja> ::= KR_IF" + ispisZavrsnog(krIf) + " L_ZAGRADA" + ispisZavrsnog(lZagrada) + " <izraz> D_ZAGRADA" + ispisZavrsnog(dZagrada) + " <naredba> KR_ELSE" + ispisZavrsnog(krElse) + " <naredba>");
				return false;
			}

			if(!naredba(naredba)) return false;

			if(!naredba(naredba2)) return false;

			return true;
		}

		return false;
	}

	private static boolean naredbaPetlje(Cvor naredbaPetlje) {

		if(provjeriProdukciju(naredbaPetlje, "KR_WHILE", "L_ZAGRADA", "<izraz>", "D_ZAGRADA", "<naredba>")) {
			Cvor krWhile = naredbaPetlje.djeca.get(0);
			Cvor lZagrada = naredbaPetlje.djeca.get(1);
			Cvor izraz = naredbaPetlje.djeca.get(2);
			Cvor dZagrada = naredbaPetlje.djeca.get(3);
			Cvor naredba = naredbaPetlje.djeca.get(4);

			if(!izraz(izraz)) return false;

			if(!castU(izraz.svojstva.get("tip"),"int")) {
				System.out.println("<naredba_petlje> ::= KR_WHILE" + ispisZavrsnog(krWhile) + " L_ZAGRADA" + ispisZavrsnog(lZagrada) + " <izraz> D_ZAGRADA" + ispisZavrsnog(dZagrada) + " <naredba>");
				return false;
			}

			if(!naredba(naredba)) return false;

			return true;
		}

		if(provjeriProdukciju(naredbaPetlje, "KR_FOR", "L_ZAGRADA", "<izraz_naredba>", "<izraz_naredba>", "D_ZAGRADA", "<naredba>")) {
			Cvor krFor = naredbaPetlje.djeca.get(0);
			Cvor lZagrada = naredbaPetlje.djeca.get(1);
			Cvor izrazNaredba = naredbaPetlje.djeca.get(2);
			Cvor izrazNaredba2 = naredbaPetlje.djeca.get(3);
			Cvor dZagrada = naredbaPetlje.djeca.get(5);
			Cvor naredba = naredbaPetlje.djeca.get(5);

			if(!izrazNaredba(izrazNaredba)) return false;

			if(!izrazNaredba(izrazNaredba2)) return false;

			if(!castU(izrazNaredba2.svojstva.get("tip"),"int")) {
				System.out.println("<naredba_petlje> ::= KR_FOR" + ispisZavrsnog(krFor) + " L_ZAGRADA" + ispisZavrsnog(lZagrada) + " <izraz_naredba> <izraz_naredba> D_ZAGRADA" + ispisZavrsnog(dZagrada) + " <naredba>");
				return false;
			}

			if(!naredba(naredba)) return false;

			return true;
		}

		if(provjeriProdukciju(naredbaPetlje, "KR_FOR", "L_ZAGRADA", "<izraz_naredba>", "<izraz_naredba>", "<izraz>", "D_ZAGRADA", "<naredba>")) {
			Cvor krFor = naredbaPetlje.djeca.get(0);
			Cvor lZagrada = naredbaPetlje.djeca.get(1);
			Cvor izrazNaredba = naredbaPetlje.djeca.get(2);
			Cvor izrazNaredba2 = naredbaPetlje.djeca.get(3);
			Cvor izraz = naredbaPetlje.djeca.get(4);
			Cvor dZagrada = naredbaPetlje.djeca.get(5);
			Cvor naredba = naredbaPetlje.djeca.get(6);

			if(!izrazNaredba(izrazNaredba)) return false;

			if(!izrazNaredba(izrazNaredba2)) return false;

			if(!castU(izrazNaredba2.svojstva.get("tip"),"int")) {
				System.out.println("<naredba_petlje> ::= KR_FOR" + ispisZavrsnog(krFor) + " L_ZAGRADA" + ispisZavrsnog(lZagrada) + " <izraz_naredba> <izraz_naredba> <izraz> D_ZAGRADA" + ispisZavrsnog(dZagrada) + " <naredba>");
				return false;
			}

			if(!izraz(izraz)) return false;

			if(!naredba(naredba)) return false;

			return true;
		}

		return false;
	}

	private static boolean naredbaSkoka(Cvor naredbaSkoka) {

		if(provjeriProdukciju(naredbaSkoka, "KR_CONTINUE", "TOCKAZAREZ") || provjeriProdukciju(naredbaSkoka, "KR_BREAK", "TOCKAZAREZ")) {
			Cvor breakilicontinue = naredbaSkoka.djeca.get(0);
			Cvor tockazarez = naredbaSkoka.djeca.get(1);

			if(!unutarPetlje(naredbaSkoka)) {
				if(breakilicontinue.znak.equals("KR_CONTINUE"))
					System.out.println("<naredba_skoka> ::= KR_CONTINUE" + ispisZavrsnog(breakilicontinue) + " TOCKAZAREZ" + ispisZavrsnog(tockazarez));
				else
					System.out.println("<naredba_skoka> ::= KR_BREAK" + ispisZavrsnog(breakilicontinue) + " TOCKAZAREZ" + ispisZavrsnog(tockazarez));

				return false;
			}

			return true;
		}

		if(provjeriProdukciju(naredbaSkoka, "KR_RETURN", "TOCKAZAREZ")) {
			Cvor krReturn = naredbaSkoka.djeca.get(0);
			Cvor tockazarez = naredbaSkoka.djeca.get(1);

			if(!unutarVoidReturnFunkcije(naredbaSkoka)) {
				System.out.println("<naredba_skoka> ::= KR_RETURN" + ispisZavrsnog(krReturn) + " TOCKAZAREZ" + ispisZavrsnog(tockazarez));
				return false;
			}

			return true;
		}

		if(provjeriProdukciju(naredbaSkoka, "KR_RETURN", "<izraz>", "TOCKAZAREZ")) {
			Cvor krReturn = naredbaSkoka.djeca.get(0);
			Cvor izraz = naredbaSkoka.djeca.get(1);
			Cvor tockazarez = naredbaSkoka.djeca.get(2);

			if(!izraz(izraz)) return false;

			String povVrijednostFunkcije = povVrijednostFunkcije(naredbaSkoka);
			if(!castU(izraz.svojstva.get("tip"),povVrijednostFunkcije)) {
				System.out.println("<naredba_skoka> ::= KR_RETURN" + ispisZavrsnog(krReturn) + " <izraz> TOCKAZAREZ" + ispisZavrsnog(tockazarez));
				return false;
			}

			return true;
		}

		return false;
	}

	private static boolean prijevodnaJedinica (Cvor prijevodnaJedinica) {

		if(provjeriProdukciju(prijevodnaJedinica, "<vanjska_deklaracija>")) {
			Cvor vanjskaDeklaracija = prijevodnaJedinica.djeca.get(0);
			return vanjskaDeklaracija(vanjskaDeklaracija);
		}

		if(provjeriProdukciju(prijevodnaJedinica, "<prijevodna_jedinica>", "<vanjska_deklaracija>")) {
			Cvor prijevodnaJedinica2 = prijevodnaJedinica.djeca.get(0);
			Cvor vanjskaDeklaracija = prijevodnaJedinica.djeca.get(1);

			if(!prijevodnaJedinica(prijevodnaJedinica2)) return false;
			return vanjskaDeklaracija(vanjskaDeklaracija);
		}

		return false;
	}

	private static boolean vanjskaDeklaracija(Cvor vanjskaDeklaracija) {
		if(provjeriProdukciju(vanjskaDeklaracija, "<definicija_funkcije>")) {
			Cvor definicijaFunkcija = vanjskaDeklaracija.djeca.get(0);
			if(!definicijaFunkcije(definicijaFunkcija)) return false;
			return true;
		}

		if(provjeriProdukciju(vanjskaDeklaracija, "<deklaracija>")) {
			Cvor deklaracija = vanjskaDeklaracija.djeca.get(0);
			if(!deklaracija(deklaracija)) return false;
			return true;
		}

		return false;
	}

	private static boolean definicijaFunkcije(Cvor definicijaFunkcije) {
		if(provjeriProdukciju(definicijaFunkcije, "<ime_tipa>", "IDN", "L_ZAGRADA", "KR_VOID", "D_ZAGRADA", "<slozena_naredba>")) {
			Cvor imeTipa = definicijaFunkcije.djeca.get(0);
			Cvor idn = definicijaFunkcije.djeca.get(1);
			Cvor lZagrada = definicijaFunkcije.djeca.get(2);
			Cvor krVoid = definicijaFunkcije.djeca.get(3);
			Cvor dZagrada = definicijaFunkcije.djeca.get(4);
			Cvor slozenaNaredba = definicijaFunkcije.djeca.get(5);

			if(!imeTipa(imeTipa)) return false;

			if(imeTipa.svojstva.get("tip").equals("const(int)") || imeTipa.svojstva.get("tip").equals("const(char)")){
				System.out.println("<definicija_funkcije> ::= <ime_tipa> IDN" + ispisZavrsnog(idn) + " L_ZAGRADA" + ispisZavrsnog(lZagrada) + " KR_VOID" + ispisZavrsnog(krVoid) +" D_ZAGRADA" + ispisZavrsnog(dZagrada) + " <slozena_naredba>");
				return false;
			}
			for(int i=0;i<globalna.znak.size();i++) {
				if(globalna.getZnak(i).equals(idn.svojstva.get("lJedinka"))
						&& globalna.getTip(i).contains("->")
						&& globalna.getDef(i)==true) {
					System.out.println("<definicija_funkcije> ::= <ime_tipa> IDN" + ispisZavrsnog(idn) + " L_ZAGRADA" + ispisZavrsnog(lZagrada) + " KR_VOID" + ispisZavrsnog(krVoid) +" D_ZAGRADA" + ispisZavrsnog(dZagrada) + " <slozena_naredba>");
					return false;
				}
			}

			int index=-1;
			for(int i=0;i<globalna.znak.size();i++) {
				if(globalna.getZnak(i).equals(idn.svojstva.get("lJedinka")) && globalna.getTip(i).contains("->")) {
					index = i;

					if(!globalna.getTip(i).equals("void->"+imeTipa.svojstva.get("tip"))) {
						System.out.println("<definicija_funkcije> ::= <ime_tipa> IDN" + ispisZavrsnog(idn) + " L_ZAGRADA" + ispisZavrsnog(lZagrada) + " KR_VOID" + ispisZavrsnog(krVoid) +" D_ZAGRADA" + ispisZavrsnog(dZagrada) + " <slozena_naredba>");
						return false;
					}
					globalna.def.set(i, true);
				}
			}

			if(index==-1){
				globalna.add(idn.svojstva.get("lJedinka"), "void->"+imeTipa.svojstva.get("tip"), false, true);
			}

			if(!slozenaNaredba(slozenaNaredba)) return false;

			return true;
		}

		if(provjeriProdukciju(definicijaFunkcije, "<ime_tipa>", "IDN", "L_ZAGRADA", "<lista_parametara>", "D_ZAGRADA", "<slozena_naredba>")) {
			Cvor imeTipa = definicijaFunkcije.djeca.get(0);
			Cvor idn = definicijaFunkcije.djeca.get(1);
			Cvor lZagrada = definicijaFunkcije.djeca.get(2);
			Cvor listaParametara = definicijaFunkcije.djeca.get(3);
			Cvor dZagrada = definicijaFunkcije.djeca.get(4);
			Cvor slozenaNaredba = definicijaFunkcije.djeca.get(5);

			if(!imeTipa(imeTipa)) return false;

			if(imeTipa.svojstva.get("tip").equals("const(int)") || imeTipa.svojstva.get("tip").equals("const(char)")) {
				System.out.println("<definicija_funkcije> ::= <ime_tipa> IDN" + ispisZavrsnog(idn) + " L_ZAGRADA" + ispisZavrsnog(lZagrada) + " <lista_parametara> D_ZAGRADA" + ispisZavrsnog(dZagrada) + " <slozena_naredba>");
				return false;
			}

			for(int i=0;i<globalna.znak.size();i++) {
				if(globalna.getZnak(i).equals(idn.svojstva.get("lJedinka"))
						&& globalna.getTip(i).contains("->")
						&& globalna.getDef(i)==true) {
					System.out.println("<definicija_funkcije> ::= <ime_tipa> IDN" + ispisZavrsnog(idn) + " L_ZAGRADA" + ispisZavrsnog(lZagrada) + " <lista_parametara> D_ZAGRADA" + ispisZavrsnog(dZagrada) + " <slozena_naredba>");
					return false;
				}
			}

			if(!listaParametara(listaParametara)) return false;

			String tipovi = listaParametara.svojstva.get("tipovi");
			tipovi = tipovi.contains(",") ? tipovi : tipovi.substring(1,tipovi.length()-1);

			int index=-1;
			for(int i=0;i<globalna.znak.size();i++) {
				if(globalna.znak.get(i).equals(idn.svojstva.get("lJedinka"))
						&& globalna.getTip(i).contains("->")) {
					index = i;

					if(!globalna.getTip(i).equals(tipovi+"->"+imeTipa.svojstva.get("tip"))) {
						System.out.println("<definicija_funkcije> ::= <ime_tipa> IDN" + ispisZavrsnog(idn) + " L_ZAGRADA" + ispisZavrsnog(lZagrada) + " <lista_parametara> D_ZAGRADA" + ispisZavrsnog(dZagrada) + " <slozena_naredba>");
						return false;
					}

					globalna.def.set(i, true);
				}
			}


			if(index==-1){
				definicijaFunkcije.tablicaZnakova.add(idn.svojstva.get("lJedinka"), tipovi+"->"+imeTipa.svojstva.get("tip"), false, true);
			}

			String imena = listaParametara.svojstva.get("imena");
			imena = imena.contains("]") ? imena.substring(1,imena.length()-1):imena ;

			tipovi = tipovi.contains("]") ? tipovi.substring(1,tipovi.length()-1) : tipovi;

			String[] imenaAr = imena.split(",");
			String[] tipoviAr = tipovi.split(",");
			for(int i=0;i<imenaAr.length;i++) {
				slozenaNaredba.tablicaZnakova.add(imenaAr[i].trim(), tipoviAr[i].trim(), true, true);
			}

			if(!slozenaNaredba(slozenaNaredba)) return false;

			return true;
		}

		return false;
	}

	private static boolean listaParametara(Cvor listaParametara) {
		if(provjeriProdukciju(listaParametara, "<deklaracija_parametra>")) {
			Cvor deklaracijaParametra = listaParametara.djeca.get(0);

			if(!deklaracijaParametra(deklaracijaParametra)) return false;

			listaParametara.svojstva.put("tipovi", "["+deklaracijaParametra.svojstva.get("tip")+"]");
			listaParametara.svojstva.put("imena", "["+deklaracijaParametra.svojstva.get("ime")+"]");

			return true;
		}

		if(provjeriProdukciju(listaParametara, "<lista_parametara>", "ZAREZ", "<deklaracija_parametra>")) {
			Cvor listaParametara2 = listaParametara.djeca.get(0);
			Cvor zarez = listaParametara.djeca.get(1);
			Cvor deklaracijaParametra = listaParametara.djeca.get(2);

			if(!listaParametara(listaParametara2)) return false;

			if(!deklaracijaParametra(deklaracijaParametra)) return false;

			if(listaParametara2.svojstva.get("imena").contains(deklaracijaParametra.svojstva.get("ime"))) {
				System.out.println("<lista_parametara> ::= <lista_parametara> ZAREZ" + ispisZavrsnog(zarez) + " <deklaracija_parametra>");
				return false;
			}

			String tipovi = listaParametara2.svojstva.get("tipovi");
			tipovi = tipovi.substring(0,tipovi.length()-1)+", "+deklaracijaParametra.svojstva.get("tip") +"]";
			listaParametara.svojstva.put("tipovi",tipovi);

			String imena = listaParametara2.svojstva.get("imena");
			imena = imena.substring(0,imena.length()-1) + ", " + deklaracijaParametra.svojstva.get("ime") + "]";
			listaParametara.svojstva.put("imena", imena);

			return true;
		}
		return false;

	}

	private static boolean deklaracijaParametra(Cvor deklaracijaParametra) {
		if(provjeriProdukciju(deklaracijaParametra, "<ime_tipa>", "IDN")) {
			Cvor imeTipa = deklaracijaParametra.djeca.get(0);
			Cvor idn = deklaracijaParametra.djeca.get(1);

			if(!imeTipa(imeTipa)) return false;

			if(imeTipa.svojstva.get("tip").equals("void")) {
				System.out.println("<deklaracija_parametra> ::= <ime_tipa> IDN" + ispisZavrsnog(idn));
				return false;
			}

			deklaracijaParametra.svojstva.put("tip", imeTipa.svojstva.get("tip"));
			deklaracijaParametra.svojstva.put("ime", idn.svojstva.get("lJedinka"));

			return true;
		}

		if(provjeriProdukciju(deklaracijaParametra, "<ime_tipa>", "IDN", "L_UGL_ZAGRADA", "D_UGL_ZAGRADA")) {
			Cvor imeTipa = deklaracijaParametra.djeca.get(0);
			Cvor idn = deklaracijaParametra.djeca.get(1);
			Cvor lUglZagrada = deklaracijaParametra.djeca.get(2);
			Cvor dUglZagrada = deklaracijaParametra.djeca.get(3);

			if(!imeTipa(imeTipa)) return false;

			if(imeTipa.svojstva.get("tip").equals("void")) {
				System.out.println("<deklaracija_parametra> ::= <ime_tipa> IDN" + ispisZavrsnog(idn) +" L_UGL_ZAGRADA" + ispisZavrsnog(lUglZagrada) + " D_UGL_ZAGRADA"+ ispisZavrsnog(dUglZagrada));
				return false;
			}

			deklaracijaParametra.svojstva.put("tip", "niz("+imeTipa.svojstva.get("tip")+")");
			deklaracijaParametra.svojstva.put("ime", idn.svojstva.get("lJedinka"));

			return true;
		}


		return false;
	}

	private static boolean listaDeklaracija(Cvor listaDeklaracija) {
		if(provjeriProdukciju(listaDeklaracija, "<deklaracija>"))
			return deklaracija(listaDeklaracija.djeca.get(0));

		if(provjeriProdukciju(listaDeklaracija, "<lista_deklaracija>", "<deklaracija>")) {
			if(!listaDeklaracija(listaDeklaracija.djeca.get(0))) return false;
			return deklaracija(listaDeklaracija.djeca.get(1));
		}

		return false;
	}

	private static boolean deklaracija(Cvor deklaracija) {
		if(provjeriProdukciju(deklaracija, "<ime_tipa>","<lista_init_deklaratora>", "TOCKAZAREZ")) {
			Cvor imeTipa = deklaracija.djeca.get(0);
			Cvor listaInitDeklaratora = deklaracija.djeca.get(1);

			if(!imeTipa(imeTipa)) return false;

			listaInitDeklaratora.svojstva.put("ntip", imeTipa.svojstva.get("tip"));
			if(!listaInitDeklaratora(listaInitDeklaratora)) return false;

			return true;
		}
		return false;
	}

	private static boolean listaInitDeklaratora(Cvor listaInitDeklaratora) {
		if(provjeriProdukciju(listaInitDeklaratora, "<init_deklarator>")) {
			Cvor initDeklarator = listaInitDeklaratora.djeca.get(0);

			initDeklarator.svojstva.put("ntip", listaInitDeklaratora.svojstva.get("ntip"));
			if(!initDeklarator(initDeklarator)) return false;

			return true;
		}

		if(provjeriProdukciju(listaInitDeklaratora, "<lista_init_deklaratora>", "ZAREZ", "<init_deklarator>")) {
			Cvor listaInitDeklaratora2 = listaInitDeklaratora.djeca.get(0);
			Cvor initDeklarator = listaInitDeklaratora.djeca.get(2);

			listaInitDeklaratora2.svojstva.put("ntip", listaInitDeklaratora.svojstva.get("ntip"));
			if(!listaInitDeklaratora(listaInitDeklaratora2)) return false;

			initDeklarator.svojstva.put("ntip", listaInitDeklaratora.svojstva.get("ntip"));
			if(!initDeklarator(initDeklarator)) return false;

			return true;
		}
		return false;
	}

	private static boolean initDeklarator(Cvor initDeklarator) {
		if(provjeriProdukciju(initDeklarator, "<izravni_deklarator>")) {
			Cvor izravniDeklarator = initDeklarator.djeca.get(0);

			izravniDeklarator.svojstva.put("ntip", initDeklarator.svojstva.get("ntip"));
			if(!izravniDeklarator(izravniDeklarator)) return false;

			String tip = izravniDeklarator.svojstva.get("tip");
			if(tip.equals("const(int)")
					|| tip.equals("const(char)")
					|| tip.equals("niz(const(char))")
					|| tip.equals("niz(const(int))")) {
				System.out.println("<init_deklarator> ::= <izravni_deklarator>");
				return false;
			}

			return true;
		}

		if(provjeriProdukciju(initDeklarator, "<izravni_deklarator>", "OP_PRIDRUZI", "<inicijalizator>")) {
			Cvor izravniDeklarator = initDeklarator.djeca.get(0);
			Cvor opPridruzi = initDeklarator.djeca.get(1);
			Cvor inicijalizator = initDeklarator.djeca.get(2);

			izravniDeklarator.svojstva.put("ntip", initDeklarator.svojstva.get("ntip"));
			if(!izravniDeklarator(izravniDeklarator)) return false;

			if(!inicijalizator(inicijalizator)) return false;

			if(izravniDeklarator.svojstva.get("tip").equals("int")
					|| izravniDeklarator.svojstva.get("tip").equals("char")
					|| izravniDeklarator.svojstva.get("tip").equals("const(int)")
					|| izravniDeklarator.svojstva.get("tip").equals("const(char)")) {

				String t = izravniDeklarator.svojstva.get("tip");
				if(t.contains("const")) t=t.substring(6,t.length()-1);

				if(!castU(inicijalizator.svojstva.get("tip"),t)) {
					System.out.println("<init_deklarator> ::= <izravni_deklarator> OP_PRIDRUZI" + ispisZavrsnog(opPridruzi) + " <inicijalizator>");
					return false;
				}
				return true;

			}else if(izravniDeklarator.svojstva.get("tip").equals("niz(int)")
					|| izravniDeklarator.svojstva.get("tip").equals("niz(char)")
					|| izravniDeklarator.svojstva.get("tip").equals("niz(const(int))")
					|| izravniDeklarator.svojstva.get("tip").equals("niz(const(char))")) {
				if(inicijalizator.svojstva.get("br-elem")==null) {
					System.out.println("<init_deklarator> ::= <izravni_deklarator> OP_PRIDRUZI" + ispisZavrsnog(opPridruzi) + " <inicijalizator>");
					return false;
				}

				if(Integer.parseInt(inicijalizator.svojstva.get("br-elem"))>Integer.parseInt(izravniDeklarator.svojstva.get("br-elem"))) {
					System.out.println("<init_deklarator> ::= <izravni_deklarator> OP_PRIDRUZI" + ispisZavrsnog(opPridruzi) + " <inicijalizator>");
					return false;
				}
				String t = izravniDeklarator.svojstva.get("tip");
				t = t.substring(4,t.length()-1);
				if(t.contains("const")) t=t.substring(6,t.length()-1);



				String tipoviString = inicijalizator.svojstva.get("tipovi");
				String[] tipovi = tipoviString.substring(1, tipoviString.length()-1).split(",");

				for(String u:tipovi) {
					if(!castU(u,t)) {
						System.out.println("<init_deklarator> ::= <izravni_deklarator> OP_PRIDRUZI" + ispisZavrsnog(opPridruzi) + " <inicijalizator>");
						return false;
					}
				}
				return true;
			}else {
				System.out.println("<init_deklarator> ::= <izravni_deklarator> OP_PRIDRUZI" + ispisZavrsnog(opPridruzi) + " <inicijalizator>");
				return false;
			}
		}
		return false;
	}

	private static boolean izravniDeklarator(Cvor izravniDeklarator) {

		if(provjeriProdukciju(izravniDeklarator, "IDN")) {
			Cvor idn = izravniDeklarator.djeca.get(0);

			if(izravniDeklarator.svojstva.get("ntip").equals("void")) {
				System.out.println("<izravni_deklarator> ::= IDN"+ispisZavrsnog(idn));
				return false;
			}
			if(izravniDeklarator.tablicaZnakova.znak.contains(idn.svojstva.get("lJedinka"))) {
				System.out.println("<izravni_deklarator> ::= IDN"+ispisZavrsnog(idn));
				return false;
			}

			izravniDeklarator.tablicaZnakova.add(idn.svojstva.get("lJedinka"), izravniDeklarator.svojstva.get("ntip"), true, true);
			izravniDeklarator.svojstva.put("tip", izravniDeklarator.svojstva.get("ntip"));

			return true;
		}

		if(provjeriProdukciju(izravniDeklarator, "IDN", "L_UGL_ZAGRADA", "BROJ", "D_UGL_ZAGRADA")) {
			Cvor idn = izravniDeklarator.djeca.get(0);
			Cvor lUglZagrada = izravniDeklarator.djeca.get(1);
			Cvor broj = izravniDeklarator.djeca.get(2);
			Cvor dUglZagrada = izravniDeklarator.djeca.get(3);

			if(izravniDeklarator.svojstva.get("ntip").equals("void")) {
				System.out.println("<izravni_deklarator> ::= IDN" + ispisZavrsnog(idn)+ " L_UGL_ZAGRADA"+ispisZavrsnog(lUglZagrada)+ " BROJ"+ispisZavrsnog(broj)+" D_UGL_ZAGRADA"+ ispisZavrsnog(dUglZagrada));
				return false;
			}

			if(izravniDeklarator.tablicaZnakova.znak.contains(idn.svojstva.get("lJedinka"))) {
				System.out.println("<izravni_deklarator> ::= IDN" + ispisZavrsnog(idn)+ " L_UGL_ZAGRADA"+ispisZavrsnog(lUglZagrada)+ " BROJ"+ispisZavrsnog(broj)+" D_UGL_ZAGRADA"+ ispisZavrsnog(dUglZagrada));
				return false;
			}

			int brojV = Integer.parseInt(broj.svojstva.get("lJedinka"));
			if(brojV<=0 || brojV>1024) {
				System.out.println("<izravni_deklarator> ::= IDN" + ispisZavrsnog(idn)+ " L_UGL_ZAGRADA"+ispisZavrsnog(lUglZagrada)+ " BROJ"+ispisZavrsnog(broj)+" D_UGL_ZAGRADA"+ ispisZavrsnog(dUglZagrada));
				return false;
			}
			izravniDeklarator.tablicaZnakova.add(idn.svojstva.get("lJedinka"),"niz("+izravniDeklarator.svojstva.get("ntip")+")", true, true);
			izravniDeklarator.svojstva.put("tip", "niz("+izravniDeklarator.svojstva.get("ntip")+")");
			izravniDeklarator.svojstva.put("br-elem",""+brojV);

			return true;
		}

		if(provjeriProdukciju(izravniDeklarator, "IDN", "L_ZAGRADA", "KR_VOID", "D_ZAGRADA")) {
			Cvor idn = izravniDeklarator.djeca.get(0);
			Cvor lZagrada = izravniDeklarator.djeca.get(1);
			Cvor krVoid = izravniDeklarator.djeca.get(2);
			Cvor dZagrada = izravniDeklarator.djeca.get(3);
			String ime = idn.svojstva.get("lJedinka");
			TablicaZnakova lokalna = izravniDeklarator.tablicaZnakova;

			int index=-1;
			for(int i=0;i<lokalna.znak.size();i++) {
				if(lokalna.getZnak(i).equals(ime) && lokalna.getTip(i).contains("->")
						&& !lokalna.getTip(i).equals("void->+"+izravniDeklarator.svojstva.get("ntip"))) {
					System.out.println("<izravni_deklarator> ::= IDN" + ispisZavrsnog(idn) + " L_ZAGRADA" + ispisZavrsnog(lZagrada) + " KR_VOID"+ ispisZavrsnog(krVoid) + " D_ZAGRADA"+ ispisZavrsnog(dZagrada));
					return false;
				}
				if(lokalna.getZnak(i).equals(ime) && lokalna.getTip(i).contains("->")
						&& lokalna.getTip(i).equals("void->+"+izravniDeklarator.svojstva.get("ntip"))) index=i;
			}

			if(index==-1) lokalna.add(ime, "void->"+izravniDeklarator.svojstva.get("ntip"), false, false);

			izravniDeklarator.svojstva.put("tip", "void->"+izravniDeklarator.svojstva.get("ntip"));

			return true;
		}

		if(provjeriProdukciju(izravniDeklarator, "IDN", "L_ZAGRADA", "<lista_parametara>", "D_ZAGRADA")) {
			Cvor idn = izravniDeklarator.djeca.get(0);
			Cvor lZagrada = izravniDeklarator.djeca.get(1);
			Cvor listaParametara = izravniDeklarator.djeca.get(2);
			Cvor dZagrada = izravniDeklarator.djeca.get(3);
			String ime = idn.svojstva.get("lJedinka");
			TablicaZnakova lokalna = izravniDeklarator.tablicaZnakova;

			if(!listaParametara(listaParametara)) return false;

			String tipovi = listaParametara.svojstva.get("tipovi");
			tipovi = tipovi.contains(",") ? tipovi : tipovi.substring(1,tipovi.length()-1);

			String tip = tipovi+"->"+izravniDeklarator.svojstva.get("ntip");

			int index=-1;
			for(int i=0;i<lokalna.znak.size();i++) {
				if(lokalna.getZnak(i).equals(ime) && lokalna.getTip(i).contains("->")
						&& !lokalna.getTip(i).equals(tip)) {
					System.out.println("<izravni_deklarator> ::= IDN"+ispisZavrsnog(idn)+" L_ZAGRADA" + ispisZavrsnog(lZagrada) + " <lista_parametara> D_ZAGRADA"+ ispisZavrsnog(dZagrada));
					return false;
				}
				if(lokalna.getZnak(i).equals(ime) && lokalna.getTip(i).contains("->")
						&& lokalna.getTip(i).equals(tip)) index=i;
			}
			if(index==-1) lokalna.add(ime, tip, false, false);
			izravniDeklarator.svojstva.put("tip", tip);
			return true;
		}
		return false;
	}

	private static boolean inicijalizator(Cvor inicijalizator) {

		if(provjeriProdukciju(inicijalizator, "<izraz_pridruzivanja>")) {
			Cvor izrazPridruzivanja = inicijalizator.djeca.get(0);

			if(!izrazPridruzivanja(izrazPridruzivanja)) return false;

			Cvor nizZnakova = pridUniz(izrazPridruzivanja);
			if(nizZnakova!=null) {
				inicijalizator.svojstva.put("br-elem", ""+(nizZnakova.svojstva.get("lJedinka").length()-1));
				ArrayList<String> tipoviArr = new ArrayList<>();
				for(int i=0;i<(nizZnakova.svojstva.get("lJedinka").length()-1);i++) {
					tipoviArr.add("char");
				}
				inicijalizator.svojstva.put("tipovi", tipoviArr.toString());
			}else {
				inicijalizator.svojstva.put("tip", izrazPridruzivanja.svojstva.get("tip"));
			}

			return true;
		}

		if(provjeriProdukciju(inicijalizator, "L_VIT_ZAGRADA", "<lista_izraza_pridruzivanja>", "D_VIT_ZAGRADA")) {
			Cvor listaIzrazaPridruzivanja = inicijalizator.djeca.get(1);
			if(!listaIzrazaPridruzivanja(listaIzrazaPridruzivanja)) return false;

			inicijalizator.svojstva.put("br-elem", listaIzrazaPridruzivanja.svojstva.get("br-elem"));
			inicijalizator.svojstva.put("tipovi", listaIzrazaPridruzivanja.svojstva.get("tipovi"));

			return true;
		}

		return false;
	}

	private static boolean listaIzrazaPridruzivanja(Cvor listaIzrazaPridruzivanja) {
		if(provjeriProdukciju(listaIzrazaPridruzivanja, "<izraz_pridruzivanja>")) {
			Cvor izrazPridruzivanja = listaIzrazaPridruzivanja.djeca.get(0);
			if(!izrazPridruzivanja(izrazPridruzivanja)) return false;
			listaIzrazaPridruzivanja.svojstva.put("tipovi", "["+izrazPridruzivanja.svojstva.get("tip")+"]");
			listaIzrazaPridruzivanja.svojstva.put("br-elem", 1+"");

			return true;
		}

		if(provjeriProdukciju(listaIzrazaPridruzivanja, "<lista_izraza_pridruzivanja>","ZAREZ", "<izraz_pridruzivanja>")) {
			Cvor listaIzrazaPridruzivanja2 = listaIzrazaPridruzivanja.djeca.get(0);
			Cvor izrazPridruzivanja = listaIzrazaPridruzivanja.djeca.get(2);
			if(!listaIzrazaPridruzivanja(listaIzrazaPridruzivanja2)) return false;
			if(!izrazPridruzivanja(izrazPridruzivanja)) return false;

			String tipovi = listaIzrazaPridruzivanja2.svojstva.get("tipovi");
			tipovi = tipovi.substring(0,tipovi.length()-1)+", " + izrazPridruzivanja.svojstva.get("tip") + "]";
			int br_elem = Integer.parseInt(listaIzrazaPridruzivanja2.svojstva.get("br-elem"))+1;
			listaIzrazaPridruzivanja.svojstva.put("tipovi", tipovi);
			listaIzrazaPridruzivanja.svojstva.put("br-elem", ""+br_elem);

			return true;
		}
		return false;
	}

}
