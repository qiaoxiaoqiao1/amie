/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amie.data;

import javatools.datatypes.Integer;

/**
 *
 * @author jlajus
 */
public class MultilingualKB extends KB {

    @Override
    protected boolean add(int subject, int relation, int object) {
        String[] split = object.toString().split("@");
        if (split.length == 2) {
            super.add(object, KB.map("<label>"), KB.map(split[0]));
            super.add(object, KB.map("<lang>"), KB.map(split[1]));
        }
        return super.add(subject, relation, object);
    }
}
