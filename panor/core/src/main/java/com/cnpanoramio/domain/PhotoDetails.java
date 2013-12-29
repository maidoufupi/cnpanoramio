package com.cnpanoramio.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PhotoDetails")
@Entity
@Table(name = "photo_details")
public class PhotoDetails {
    
	@MapsId
    @OneToOne
    @JoinColumn(name="id")
	private Photo photo;
	
	@Id
    private Long id;

/* Origin */
	// 拍摄时间
	private String DateTimeOriginal;
	private String DateTimeDigitized;
	private String DateTime;
		
/* Image */
	private String pixelXDimension;
	private String pixelYDimension;
	private String xResolution;
	private String yResolution;


	private String resolutionUnit;

	private String compressedBitsPerPixel;
    
	
/* Camera */
	private String make;
	private String model;
    
	// 曝光时间 即快门速度
	private String exposureTime;
	
	// FNumber光圈系数
	private Double FNumber;
    
	// ExposureBiasValue曝光补偿
	private Integer ExposureBiasValue;
	
	// 焦距
	private Double FocalLength;
	
    // 最大光圈
	private Double maxApertureValue;
	
	// 测光方式， 平均式测光、中央重点测光、点测光等。
	private String meteringMode;
	
	// 焦距
	private Double focalLengthIn35mmFilm;
	
/* Advanced photo */
	private String contrast;
	private Double brightnessValue;
	private String lightSource;
	private String exposureProgram;
	private String saturation;
	private String sharpness;
	private String whiteBalance;
	
	// 感光度 (called ISOSpeedRatings by EXIF 2.2, then PhotographicSensitivity by the EXIF 2.3 spec.)
	private String ISO;

	private Integer digitalZoomRatio;
	private String ExifVersion;
	
/* GPS */
	private String GPSLatitudeRef;
	private Double GPSLatitude;
	private String GPSLongitudeRef;
	private Double GPSLongitude;
	private String GPSAltitudeRef;
	private Double GPSAltitude;
	private Double GPSTimeStamp;
	private String GPSProcessingMethod;
	private Date GPSDateStamp;
	public Photo getPhoto() {
		return photo;
	}
	public void setPhoto(Photo photo) {
		this.photo = photo;
//		this.id = photo.getId();
	}
	
//	@Id 
//	@GenericGenerator(name="fk1", strategy = "foreign", 
//	  parameters={ @Parameter (name="property", value="photo") }
//	)
//	@GeneratedValue(strategy = GenerationType.AUTO, generator="fk1")  
//	@GenericGenerator(strategy="foreign", name = "foreign")
	
	public Long getId() {
		return id;
	}
	public void setId(Long photo_id) {
		this.id = photo_id;
	}
	
	public String getPixelXDimension() {
		return pixelXDimension;
	}
	public void setPixelXDimension(String pixelXDimension) {
		this.pixelXDimension = pixelXDimension;
	}
	public String getPixelYDimension() {
		return pixelYDimension;
	}
	public void setPixelYDimension(String pixelYDimension) {
		this.pixelYDimension = pixelYDimension;
	}
	public String getxResolution() {
		return xResolution;
	}
	public void setxResolution(String xResolution) {
		this.xResolution = xResolution;
	}
	public String getyResolution() {
		return yResolution;
	}
	public void setyResolution(String yResolution) {
		this.yResolution = yResolution;
	}
	public String getResolutionUnit() {
		return resolutionUnit;
	}
	public void setResolutionUnit(String resolutionUnit) {
		this.resolutionUnit = resolutionUnit;
	}
	public String getCompressedBitsPerPixel() {
		return compressedBitsPerPixel;
	}
	public void setCompressedBitsPerPixel(String compressedBitsPerPixel) {
		this.compressedBitsPerPixel = compressedBitsPerPixel;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getExposureTime() {
		return exposureTime;
	}
	public void setExposureTime(String exposureTime) {
		this.exposureTime = exposureTime;
	}
		
	public Double getFNumber() {
		return FNumber;
	}
	public void setFNumber(Double fNumber) {
		this.FNumber = fNumber;
	}
		
	public Integer getExposureBiasValue() {
		return ExposureBiasValue;
	}
	public void setExposureBiasValue(Integer exposureBiasValue) {
		ExposureBiasValue = exposureBiasValue;
	}
	public Double getFocalLength() {
		return FocalLength;
	}
	public void setFocalLength(Double focalLength) {
		this.FocalLength = focalLength;
	}
	public Double getMaxApertureValue() {
		return maxApertureValue;
	}
	public void setMaxApertureValue(Double maxApertureValue) {
		this.maxApertureValue = maxApertureValue;
	}
	public String getMeteringMode() {
		return meteringMode;
	}
	public void setMeteringMode(String meteringMode) {
		this.meteringMode = meteringMode;
	}
	public Double getFocalLengthIn35mmFilm() {
		return focalLengthIn35mmFilm;
	}
	public void setFocalLengthIn35mmFilm(Double focalLengthIn35mmFilm) {
		this.focalLengthIn35mmFilm = focalLengthIn35mmFilm;
	}
	public String getContrast() {
		return contrast;
	}
	public void setContrast(String contrast) {
		this.contrast = contrast;
	}
	public Double getBrightnessValue() {
		return brightnessValue;
	}
	public void setBrightnessValue(Double brightnessValue) {
		this.brightnessValue = brightnessValue;
	}
	public String getLightSource() {
		return lightSource;
	}
	public void setLightSource(String lightSource) {
		this.lightSource = lightSource;
	}
	public String getExposureProgram() {
		return exposureProgram;
	}
	public void setExposureProgram(String exposureProgram) {
		this.exposureProgram = exposureProgram;
	}
	public String getSaturation() {
		return saturation;
	}
	public void setSaturation(String saturation) {
		this.saturation = saturation;
	}
	public String getSharpness() {
		return sharpness;
	}
	public void setSharpness(String sharpness) {
		this.sharpness = sharpness;
	}
	public String getWhiteBalance() {
		return whiteBalance;
	}
	public void setWhiteBalance(String whiteBalance) {
		this.whiteBalance = whiteBalance;
	}
	public Integer getDigitalZoomRatio() {
		return digitalZoomRatio;
	}
	public void setDigitalZoomRatio(Integer digitalZoomRatio) {
		this.digitalZoomRatio = digitalZoomRatio;
	}
	public String getExifVersion() {
		return ExifVersion;
	}
	public void setExifVersion(String exifVersion) {
		this.ExifVersion = exifVersion;
	}
	public String getGPSLatitudeRef() {
		return GPSLatitudeRef;
	}
	public void setGPSLatitudeRef(String gPSLatitudeRef) {
		GPSLatitudeRef = gPSLatitudeRef;
	}
	public Double getGPSLatitude() {
		return GPSLatitude;
	}
	public void setGPSLatitude(Double gPSLatitude) {
		GPSLatitude = gPSLatitude;
	}
	public String getGPSLongitudeRef() {
		return GPSLongitudeRef;
	}
	public void setGPSLongitudeRef(String gPSLongitudeRef) {
		GPSLongitudeRef = gPSLongitudeRef;
	}
	public Double getGPSLongitude() {
		return GPSLongitude;
	}
	public void setGPSLongitude(Double gPSLongitude) {
		GPSLongitude = gPSLongitude;
	}
	public String getGPSAltitudeRef() {
		return GPSAltitudeRef;
	}
	public void setGPSAltitudeRef(String gPSAltitudeRef) {
		GPSAltitudeRef = gPSAltitudeRef;
	}
	public Double getGPSAltitude() {
		return GPSAltitude;
	}
	public void setGPSAltitude(Double gPSAltitude) {
		GPSAltitude = gPSAltitude;
	}
	public Double getGPSTimeStamp() {
		return GPSTimeStamp;
	}
	public void setGPSTimeStamp(Double gPSTimeStamp) {
		GPSTimeStamp = gPSTimeStamp;
	}
	public String getGPSProcessingMethod() {
		return GPSProcessingMethod;
	}
	public void setGPSProcessingMethod(String gPSProcessingMethod) {
		GPSProcessingMethod = gPSProcessingMethod;
	}
	public Date getGPSDateStamp() {
		return GPSDateStamp;
	}
	public void setGPSDateStamp(Date gPSDateStamp) {
		GPSDateStamp = gPSDateStamp;
	}
	public String getDateTimeOriginal() {
		return DateTimeOriginal;
	}
	public void setDateTimeOriginal(String dateTimeOriginal) {
		DateTimeOriginal = dateTimeOriginal;
	}
	public String getDateTimeDigitized() {
		return DateTimeDigitized;
	}
	public void setDateTimeDigitized(String dateTimeDigitized) {
		DateTimeDigitized = dateTimeDigitized;
	}
	public String getDateTime() {
		return DateTime;
	}
	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}
	public String getISO() {
		return ISO;
	}
	public void setISO(String iSO) {
		ISO = iSO;
	}

	
	

}
