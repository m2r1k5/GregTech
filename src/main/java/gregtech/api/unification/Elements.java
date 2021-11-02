package gregtech.api.unification;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.Map;

@ZenClass("mods.gregtech.material.Elements")
@ZenRegister
public class Elements {

    /**
     * Maximum number of elements that are allowed to be registered.
     */
    public static final int MAX_ELEMENTS = 250;

    //private static final GTControlledRegistry<String, Element> ELEMENTS = new GTControlledRegistry<>(MAX_ELEMENTS);

    private static final Map<String, Element> elements = new HashMap<>();

    private Elements() {
    }

    // TODO remove
    public static final Element He3 = add(2, 1, "Helium-3", "He-3");
    public static final Element U238 = add(92, 146, "Uranium-238", "U-238");
    public static final Element U235 = add(92, 143, "Uranium-235", "U-235");
    public static final Element Pu239 = add(94, 145, "Plutonium-239", "Pu-239");
    public static final Element Pu241 = add(94, 149, "Plutonium-241", "Pu-241");

    public static final Element H = add(1, 0, "Hydrogen", "H", 1, 7);
    public static final Element He = add(2, 2, "Helium", "He", 2, 10, 3);
    public static final Element Li = add(3, 4, "Lithium", "Li", 3, 12);
    public static final Element Be = add(4, 5, "Beryllium", "Be", 5, 16);
    public static final Element B = add(5, 5, "Boron", "B", 6, 19);
    public static final Element C = add(6, 6, "Carbon", "C", 8, 22);
    public static final Element N = add(7, 7, "Nitrogen", "N", 10, 25);
    public static final Element O = add(8, 8, "Oxygen", "O", 12, 28);
    public static final Element F = add(9, 9, "Fluorine", "F", 14, 31);
    public static final Element Ne = add(10, 10, "Neon", "Ne", 16, 34);
    public static final Element Na = add(11, 11, "Sodium", "Na", 18, 37);
    public static final Element Mg = add(12, 12, "Magnesium", "Mg", 19, 40);
    public static final Element Al = add(13, 13, "Aluminium", "Al", 21, 42);
    public static final Element Si = add(14, 14, "Silicon", "Si", 22, 44);
    public static final Element P = add(15, 15, "Phosphorus", "P", 24, 46);
    public static final Element S = add(16, 16, "Sulfur", "S", 26, 49);
    public static final Element Cl = add(17, 18, "Chlorine", "Cl", 28, 51);
    public static final Element Ar = add(18, 22, "Argon", "Ar", 30, 53);
    public static final Element K = add(19, 20, "Potassium", "K", 32, 55);
    public static final Element Ca = add(20, 20, "Calcium", "Ca", 34, 57);
    public static final Element Sc = add(21, 24, "Scandium", "Sc", 36, 60);
    public static final Element Ti = add(22, 26, "Titanium", "Ti", 38, 63);
    public static final Element V = add(23, 28, "Vanadium", "V", 40, 65);
    public static final Element Cr = add(24, 28, "Chrome", "Cr", 42, 67);
    public static final Element Mn = add(25, 30, "Manganese", "Mn", 44, 69);
    public static final Element Fe = add(26, 30, "Iron", "Fe", 45, 72);
    public static final Element Co = add(27, 32, "Cobalt", "Co", 47, 75);
    public static final Element Ni = add(28, 30, "Nickel", "Ni", 48, 78);
    public static final Element Cu = add(29, 34, "Copper", "Cu", 52, 80);
    public static final Element Zn = add(30, 35, "Zinc", "Zn", 54, 83);
    public static final Element Ga = add(31, 39, "Gallium", "Ga", 56, 86);
    public static final Element Ge = add(32, 40, "Germanium", "Ge", 58, 89);
    public static final Element As = add(33, 42, "Arsenic", "As", 60, 92);
    public static final Element Se = add(34, 45, "Selenium", "Se", 65, 94);
    public static final Element Br = add(35, 45, "Bromine", "Br", 67, 97);
    public static final Element Kr = add(36, 48, "Krypton", "Kr", 69, 100);
    public static final Element Rb = add(37, 48, "Rubidium", "Rb", 71, 102);
    public static final Element Sr = add(38, 49, "Strontium", "Sr", 73, 105);
    public static final Element Y = add(39, 50, "Yttrium", "Y", 76, 108);
    public static final Element Zr = add(40, 51, "Zirconium", "Zr", 78, 110);
    public static final Element Nb = add(41, 53, "Niobium", "Nb", 81, 113);
    public static final Element Mo = add(42, 53, "Molybdenum", "Mo", 83, 115);
    public static final Element Tc = add(43, 55, "Technetium", "Tc", 85, 118);
    public static final Element Ru = add(44, 57, "Ruthenium", "Ru", 87, 120);
    public static final Element Rh = add(45, 58, "Rhodium", "Rh", 89, 122);
    public static final Element Pd = add(46, 60, "Palladium", "Pd", 91, 124);
    public static final Element Ag = add(47, 60, "Silver", "Ag", 93, 130);
    public static final Element Cd = add(48, 64, "Cadmium", "Cd", 95, 132);
    public static final Element In = add(49, 65, "Indium", "In", 97, 135);
    public static final Element Sn = add(50, 68, "Tin", "Sn", 99, 137);
    public static final Element Sb = add(51, 70, "Antimony", "Sb", 103, 139);
    public static final Element Te = add(52, 75, "Tellurium", "Te", 105, 142);
    public static final Element I = add(53, 74, "Iodine", "I", 108, 144);
    public static final Element Xe = add(54, 77, "Xenon", "Xe", 110, 147);
    public static final Element Cs = add(55, 77, "Caesium", "Cs", 112, 151);
    public static final Element Ba = add(56, 81, "Barium", "Ba", 114, 153);
    public static final Element La = add(57, 81, "Lanthanum", "La", 117, 155);
    public static final Element Ce = add(58, 82, "Cerium", "Ce", 119, 157);
    public static final Element Pr = add(59, 81, "Praseodymium", "Pr", 121, 159);
    public static final Element Nd = add(60, 84, "Neodymium", "Nd", 124, 161);
    public static final Element Pm = add(61, 83, "Promethium", "Pm", 126, 163);
    public static final Element Sm = add(62, 88, "Samarium", "Sm", 128, 165);
    public static final Element Eu = add(63, 88, "Europium", "Eu", 130, 167);
    public static final Element Gd = add(64, 93, "Gadolinium", "Gd", 134, 169);
    public static final Element Tb = add(65, 93, "Terbium", "Tb", 136, 171);
    public static final Element Dy = add(66, 96, "Dysprosium", "Dy", 138, 173);
    public static final Element Ho = add(67, 97, "Holmium", "Ho", 140, 175);
    public static final Element Er = add(68, 99, "Erbium", "Er", 143, 177);
    public static final Element Tm = add(69, 99, "Thulium", "Tm", 145, 179);
    public static final Element Yb = add(70, 103, "Ytterbium", "Yb", 148, 182);
    public static final Element Lu = add(71, 103, "Lutetium", "Lu", 150, 184);
    public static final Element Hf = add(72, 106, "Hafnium", "Hf", 153, 188);
    public static final Element Ta = add(73, 107, "Tantalum", "Ta", 155, 190);
    public static final Element W = add(74, 109, "Tungsten", "W", 158, 192);
    public static final Element Re = add(75, 111, "Rhenium", "Re", 160, 194);
    public static final Element Os = add(76, 114, "Osmium", "Os", 162, 196);
    public static final Element Ir = add(77, 115, "Iridium", "Ir", 164, 199);
    public static final Element Pt = add(78, 117, "Platinum", "Pt", 166, 202);
    public static final Element Au = add(79, 117, "Gold", "Au", 169, 205);
    public static final Element Hg = add(80, 120, "Mercury", "Hg", 171, 210);
    public static final Element Tl = add(81, 123, "Thallium", "Tl", 176, 212);
    public static final Element Pb = add(82, 125, "Lead", "Pb", 178, 215);
    public static final Element Bi = add(83, 125, "Bismuth", "Bi", 184, 219);
    public static final Element Po = add(84, 124, "Polonium", "Po", 188, 220);
    public static final Element At = add(85, 124, "Astatine", "At", 193, 223);
    public static final Element Rn = add(86, 134, "Radon", "Rn", 195, 228);
    public static final Element Fr = add(87, 134, "Francium", "Fr", 199, 232);
    public static final Element Ra = add(88, 136, "Radium", "Ra", 202, 234);
    public static final Element Ac = add(89, 136, "Actinium", "Ac", 206, 236);
    public static final Element Th = add(90, 140, "Thorium", "Th", 209, 238);
    public static final Element Pa = add(91, 138, "Protactinium", "Pa", 212, 240);
    public static final Element U = add(92, 146, "Uranium", "U", 217, 242, 235, 238);
    public static final Element Np = add(93, 144, "Neptunium", "Np", 225, 244);
    public static final Element Pu = add(94, 152, "Plutonium", "Pu", 228, 247, 239, 241);
    public static final Element Am = add(95, 150, "Americium", "Am", 231, 249);
    public static final Element Cm = add(96, 153, "Curium", "Cm", 233, 252);
    public static final Element Bk = add(97, 152, "Berkelium", "Bk", 235, 254);
    public static final Element Cf = add(98, 153, "Californium", "Cf", 237, 256);
    public static final Element Es = add(99, 153, "Einsteinium", "Es", 240, 258);
    public static final Element Fm = add(100, 157, "Fermium", "Fm", 242, 260);
    public static final Element Md = add(101, 157, "Mendelevium", "Md", 244, 262);
    public static final Element No = add(102, 157, "Nobelium", "No", 248, 264);
    public static final Element Lr = add(103, 159, "Lawrencium", "Lr", 251, 266);
    public static final Element Rf = add(104, 161, "Rutherfordium", "Rf", 253, 268);
    public static final Element Db = add(105, 163, "Dubnium", "Db", 255, 270);
    public static final Element Sg = add(106, 165, "Seaborgium", "Sg", 258, 273);
    public static final Element Bh = add(107, 163, "Bohrium", "Bh", 260, 275);
    public static final Element Hs = add(108, 169, "Hassium", "Hs", 263, 277);
    public static final Element Mt = add(109, 167, "Meitnerium", "Mt", 265, 279);
    public static final Element Ds = add(110, 171, "Darmstadtium", "Ds", 267, 281);
    public static final Element Rg = add(111, 169, "Roentgenium", "Rg", 272, 283);
    public static final Element Cn = add(112, 173, "Copernicium", "Cn", 277, 285);
    public static final Element Nh = add(113, 171, "Nihonium", "Nh", 283, 287);
    public static final Element Fl = add(114, 175, "Flerovium", "Fl", 285, 289);
    public static final Element Mc = add(115, 173, "Moscovium", "Mc", 287, 291);
    public static final Element Lv = add(116, 177, "Livermorium", "Lv", 289, 292);
    public static final Element Ts = add(117, 177, "Tennessine", "Ts", 291, 294);
    public static final Element Og = add(118, 176, "Oganesson", "Og", 293, 293);

    //fantasy
    public static final Element Tr = add(119, 178, "Tritanium", "Tr");
    public static final Element Dr = add(120, 180, "Duranium", "Dr");
    public static final Element Nq = add(121, 172, "Naquadah", "Nq");
    public static final Element Nt = add(0, 5000, "Neutronium", "Nt");
    public static final Element Ke = add(1000, 1500, "Trinium", "Ke");
    public static final Element Ad = add(750, 1000, "Adamantium", "Ad");
    public static final Element Vb = add(850, 900, "Vibranium", "Vb");
    public static final Element Tn = add(550, 670, "Taranium", "Tn");

    // Special-case isotopes
    public static final Element D = add(1, 1, "Deuterium", "D");
    public static final Element T = add(1, 2, "Tritium", "T");
    public static final Element Nq1 = add(121, 172, "NaquadahEnriched", "Nq+");
    public static final Element Nq2 = add(121, 172, "Naquadria", "*Nq*");

    @ZenMethod // todo
    public static Element add(long protons, long neutrons, String name, String symbol, int isotopeStart, int isotopeEnd, int... generatedIsotopes) {
        Element element = new Element(protons, neutrons, name, symbol, isotopeStart, isotopeEnd, generatedIsotopes);
        //ELEMENTS.register(name, element);
        elements.put(name, element);
        return element;
    }

    public static Element add(long protons, long neutrons, String name, String symbol) {
        return add(protons, neutrons, name, symbol, 0, 0);
    }

    @ZenMethod
    public static Element get(String name) {
        return elements.get(name);
    }
}
