package gregtech.api.unification.element;

import gregtech.api.unification.Element;
import gregtech.api.unification.material.Material;

public class Isotope {

    public final int neutrons;
    public boolean enableUnification;

    // may not need these?
    private Element isotopeOf;
    private Material isotopeMaterial;

    public Isotope(int neutrons, boolean enableUnification) {
        this.neutrons = neutrons;
        this.enableUnification = enableUnification;
    }

    // internal use only
    public void setElement(Element element) {
        this.isotopeOf = element;
    }
}
