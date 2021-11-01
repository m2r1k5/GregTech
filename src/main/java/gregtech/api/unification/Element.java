package gregtech.api.unification;

import crafttweaker.annotations.ZenRegister;
import gregtech.api.unification.element.Isotope;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is some kind of Periodic Table, which can be used to determine Properties of the Materials.
 */
@ZenClass("mods.gregtech.material.Element")
@ZenRegister
public class Element {

    public final String name;
    public final String symbol;
    public final long protons;
    public final long neutrons;
    public final long mass;

    // Maximum of 256 isotopes per element
    private final int isotopeStart;
    private final int isotopeEnd;
    private final Map<Integer, Isotope> isotopes = new HashMap<>();

    // TODO - Isotope System
    // TODO -   - Keep a list of Protons for the isotopes
    // TODO -   - Materials with an Element should be in their own MetaItem "gregtech:element"
    // TODO -   - Upper limit to total isotope count per element?
    // TODO -   - Figure out how to separate which material items generate for isotopes
    // TODO -
    // TODO -   - Don't register isotopes in the MATERIAL_REGISTRY, only hold reference to them within their registered "main" material
    // TODO -   - Call something like "Uranium.getIsotope(238)" which returns a Material
    /**
     * @param protons           Amount of Protons
     * @param neutrons          Amount of Neutrons (I could have made mistakes with the Neutron amount calculation, please tell me if I did something wrong)
     * @param name              Name of the Element
     * @param symbol            Symbol of the Element
     * @param isotopeStart      The smallest atomic mass of this element possible
     * @param isotopeEnd        The largest atomic mass of this element possible
     * @param generatedIsotopes The specific isotope numbers which will have items generated for them
     */
    public Element(long protons, long neutrons, String name, String symbol, int isotopeStart, int isotopeEnd, int... generatedIsotopes) {
        this.protons = protons;
        this.neutrons = neutrons;
        this.mass = protons + neutrons;
        this.name = name;
        this.symbol = symbol;
        this.isotopeStart = isotopeStart;
        this.isotopeEnd = isotopeEnd;
        if (isotopeStart > 0 && isotopeStart < isotopeEnd) {
            addGeneratedIsotopes(generatedIsotopes);
        }
    }

    public void addGeneratedIsotopes(int... isotopes) {
        for (int i : isotopes) {
            if (i < isotopeStart || i > isotopeEnd || this.isotopes.containsKey(i)) continue;
            Isotope isotope = new Isotope(i, true);
            isotope.setElement(this);
            this.isotopes.put(i, isotope);
        }
    }

    public int getMaximumIsotopes() {
        if (isotopeStart <= 0 || isotopeStart > isotopeEnd) return 0;
        return isotopeEnd - isotopeStart + 1;
    }

    public List<Isotope> getIsotopes() {
        return new ArrayList<>(isotopes.values());
    }

    @ZenGetter("name")
    public String getName() {
        return name;
    }

    @ZenGetter("symbol")
    public String getSymbol() {
        return symbol;
    }

    @ZenGetter("protons")
    public long getProtons() {
        return protons;
    }

    @ZenGetter("neutrons")
    public long getNeutrons() {
        return neutrons;
    }

    @ZenGetter("mass")
    public long getMass() {
        return mass;
    }

    @ZenGetter("hasIsotopes")
    public boolean hasIsotopes() {
        return !isotopes.isEmpty();
    }

    @Override
    @ZenMethod
    public String toString() {
        return super.toString();
    }
}
