package gregtech.api.unification.element;

import gregtech.api.unification.Element;

public class Isotope {

    public final int neutrons;
    public boolean enableUnification;
    private Element isotopeOf;
    public boolean shouldGenerate;

    public Isotope(int neutrons, boolean shouldGenerate, boolean enableUnification) {
        this.neutrons = neutrons;
        this.enableUnification = enableUnification;
        this.shouldGenerate = shouldGenerate;
    }

    public Isotope(int neutrons) {
        this(neutrons, false, false);
    }
}
