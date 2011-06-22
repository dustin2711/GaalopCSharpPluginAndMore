package de.gaalop.gapp.instructionSet;

import de.gaalop.gapp.Selectorset;
import de.gaalop.gapp.VariableGetter;
import de.gaalop.gapp.variables.GAPPMultivector;
import de.gaalop.gapp.visitor.GAPPVisitor;

/**
Copies the selected blades from multivector mvsrc to
multivector mvdest . dest0 , src0 , dest1 , src1 , up to dest31
and src31 , are blade selectors. Note that it is invalid language syntax 
to have more than one source multivector speciﬁed in this command. 
To copy elements from several multivectors it is required to use multiple setMv()commands, 
one for each multivector. 
sThis function isrestricted to one source and destination multivector.

 */
public class GAPPSetMv extends GAPPBaseInstruction {

    private GAPPMultivector destinationMv;
    private GAPPMultivector sourceMv;
    private Selectorset selectorsDest;
    private Selectorset selectorsSrc;

    public GAPPSetMv(String parseArguments, VariableGetter getter) {
        parseFromString(parseArguments, getter);
    }

    public GAPPSetMv(GAPPMultivector destinationMv, GAPPMultivector sourceMv, Selectorset selectorsDest, Selectorset selectorsSrc) {
        this.destinationMv = destinationMv;
        this.sourceMv = sourceMv;
        this.selectorsDest = selectorsDest;
        this.selectorsSrc = selectorsSrc;
        assert (selectorsDest.size() == selectorsSrc.size());

    }

    @Override
    public void parseFromString(String toParse, VariableGetter getter) {
        String[] partsEquation = toParse.split("=");

        //Parse left side
        selectorsDest = new Selectorset();
        destinationMv = parseMultivectorWithSelectors(partsEquation[0].trim(), selectorsDest, getter);

        //Parse right side
        selectorsSrc = new Selectorset();
        sourceMv = parseMultivectorWithSelectors(partsEquation[1].trim(), selectorsSrc, getter);
    }

    @Override
    public void accept(GAPPVisitor visitor, Object arg) {
        visitor.visitSetMv(this, arg);
    }

    public GAPPMultivector getDestinationMv() {
        return destinationMv;
    }

    public Selectorset getSelectorsDest() {
        return selectorsDest;
    }

    public Selectorset getSelectorsSrc() {
        return selectorsSrc;
    }

    public GAPPMultivector getSourceMv() {
        return sourceMv;
    }
}
