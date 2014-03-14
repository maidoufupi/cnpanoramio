package com.cnpanoramio.json;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.cnpanoramio.domain.Tag;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Tags extends ArrayList<Tag> {
	
}
