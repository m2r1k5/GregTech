package gregtech.api.unification;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private final List<Integer> isotopes = new ArrayList<>();

    // TODO - Remove "halfLifeSeconds," "isIsotope," and "decayTo," rework to new system
    //
    // TODO - Isotope System
    // TODO -   - Keep a list of Protons for the isotopes
    // TODO -   - Materials with an Element should be in their own MetaItem "gregtech:element"
    // TODO -   - Upper limit to total isotope count per element?
    // TODO -   - Figure out how to separate which material items generate for isotopes
    // TODO -   -
    /**
     * @param protons         Amount of Protons
     * @param neutrons        Amount of Neutrons (I could have made mistakes with the Neutron amount calculation, please tell me if I did something wrong)
     * @param name            Name of the Element
     * @param symbol          Symbol of the Element
     * @param isotopes        A List of Strings which are the isotope's name, not including the Element name. For example:
     *                            Uranium: "235", "238", ..
     */
    public Element(long protons, long neutrons, String name, String symbol, int... isotopes) {
        this.protons = protons;
        this.neutrons = neutrons;
        this.mass = protons + neutrons;
        this.name = name;
        this.symbol = symbol;
        addIsotopes(isotopes);
    }

    @SuppressWarnings("unused")
    public void addIsotopes(int... isotopes) {
        this.isotopes.addAll(Arrays.stream(isotopes).boxed().collect(Collectors.toList()));
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
