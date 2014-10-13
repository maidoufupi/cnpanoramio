package com.cnpanoramio.domain;

import java.util.Date;

import javax.persistence.Column;
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
	private Date DateTimeOriginal;
	private Date DateTimeDigitized;
	private Date DateTime;
		
/* Image */
	@Column(nullable = true)
	private Integer pixelXDimension;
	@Column(nullable = true)
	private Integer pixelYDimension;
	private String xResolution;
	private String yResolution;

	private String resolutionUnit;

	private String compressedBitsPerPixel;

	// 方向
	@Column(length=1)
	private short Orientation;
    
	
/* Camera */
	// 相机品牌
	private String make;
	// 相机型号
	private String model;
    
	// 曝光时间 即快门速度
	private Double exposureTime;
	
	// FNumber光圈系数
	private Double FNumber;
    
	// ExposureBias曝光补偿
	private Double ExposureBias;
	
	// Flash闪光灯
	private Short Flash;
	
	// 焦距
	private Double FocalLength;
	
    // 最大光圈
	private Double maxApertureValue;
	
	// 测光方式， 平均式测光、中央重点测光、点测光等。
	private String meteringMode;
	
	// 焦距
	private Double focalLengthIn35mmFilm;
	
/* Advanced photo */
	// 对比度
	private String contrast;
	// 亮度
	private Double brightnessValue;
	// 光源 指白平衡设置
	private String lightSource;
	// 曝光程序
	private String exposureProgram;
	// 饱和度
	private String saturation;
	// 锐化
	private String sharpness;
	// 白平衡
	private String whiteBalance;
	
	// 感光度 (called ISOSpeedRatings by EXIF 2.2, then PhotographicSensitivity by the EXIF 2.3 spec.)
	private String ISO;

	// 数字变焦比率
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
	private String GPSInfo;
	
	/*
	 * <x:xmpmeta xmlns:x="adobe:ns:meta/" x:xmptk="Adobe XMP Core 5.1.0-jc003">
		<rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
			<rdf:Description rdf:about="" xmlns:GPano="http://ns.google.com/photos/1.0/panorama/">
			  <GPano:CaptureSoftware>Visualization.SE.HTC</GPano:CaptureSoftware>
			  <GPano:StitchingSoftware>Visualization.SE.HTC</GPano:StitchingSoftware>
			  <GPano:UsePanoramaViewer>True</GPano:UsePanoramaViewer>
			  <GPano:ProjectionType>equirectangular</GPano:ProjectionType>
			  <GPano:CroppedAreaLeftPixels>0</GPano:CroppedAreaLeftPixels>
			  <GPano:CroppedAreaTopPixels>0</GPano:CroppedAreaTopPixels>
			  <GPano:CroppedAreaImageWidthPixels>4096</GPano:CroppedAreaImageWidthPixels>
			  <GPano:CroppedAreaImageHeightPixels>2048</GPano:CroppedAreaImageHeightPixels>
			  <GPano:FullPanoWidthPixels>4096</GPano:FullPanoWidthPixels>
			  <GPano:FullPanoHeightPixels>2048</GPano:FullPanoHeightPixels>
			  <GPano:NorthPosInX>0</GPano:NorthPosInX>
			  <GPano:LargestValidInteriorRectLeft>0</GPano:LargestValidInteriorRectLeft>
			  <GPano:LargestValidInteriorRectTop>0</GPano:LargestValidInteriorRectTop>
			  <GPano:LargestValidInteriorRectWidth>4096</GPano:LargestValidInteriorRectWidth>
			  <GPano:LargestValidInteriorRectHeight>2048</GPano:LargestValidInteriorRectHeight>
			</rdf:Description>
		</rdf:RDF>
		</x:xmpmeta>
	 */
	//GPano:CaptureSoftware; //Visualization.SE.HTC
	 private String gPanoCaptureSoftware; //
	//GPano:StitchingSoftware; //Visualization.SE.HTC
	 private String gPanoStitchingSoftware; //
	//GPano:UsePanoramaViewer; //True
	 private String gPanoUsePanoramaViewer; //
	//GPano:ProjectionType; //equirectangular
	 private String gPanoProjectionType; //
	//GPano:CroppedAreaLeftPixels; //0
	 private String gPanoCroppedAreaLeftPixels; //
	//GPano:CroppedAreaTopPixels; //0
	 private String gPanoCroppedAreaTopPixels; //
	//GPano:CroppedAreaImageWidthPixels; //4096
	 private String gPanoCroppedAreaImageWidthPixels; //
	//GPano:CroppedAreaImageHeightPixels; //2048
	 private String gPanoCroppedAreaImageHeightPixels; //
	//GPano:FullPanoWidthPixels; //4096
	 private String gPanoFullPanoWidthPixels; //
	//GPano:FullPanoHeightPixels; //2048
	 private String gPanoFullPanoHeightPixels; //
	//GPano:NorthPosInX; //0
	 private String gPanoNorthPosInX; //
	//GPano:LargestValidInteriorRectLeft; //0
	 private String gPanoLargestValidInteriorRectLeft; //
	//GPano:LargestValidInteriorRectTop; //0
	 private String gPanoLargestValidInteriorRectTop; //
	//GPano:LargestValidInteriorRectWidth; //4096
	 private String gPanoLargestValidInteriorRectWidth; //
	//GPano:LargestValidInteriorRectHeight; //2048
	 private String gPanoLargestValidInteriorRectHeight; //
	
	public Photo getPhoto() {
		return photo;
	}
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
		
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getPixelXDimension() {
		return pixelXDimension;
	}
	public void setPixelXDimension(Integer pixelXDimension) {
		this.pixelXDimension = pixelXDimension;
	}
	public Integer getPixelYDimension() {
		return pixelYDimension;
	}
	public void setPixelYDimension(Integer pixelYDimension) {
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
	public Double getExposureTime() {
		return exposureTime;
	}
	public void setExposureTime(Double exposureTime) {
		this.exposureTime = exposureTime;
	}
		
	public Double getFNumber() {
		return FNumber;
	}
	public void setFNumber(Double fNumber) {
		this.FNumber = fNumber;
	}		

	public Double getExposureBias() {
		return ExposureBias;
	}
	public void setExposureBias(Double exposureBias) {
		ExposureBias = exposureBias;
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
	
	public Date getDateTimeOriginal() {
		return DateTimeOriginal;
	}
	public void setDateTimeOriginal(Date dateTimeOriginal) {
		DateTimeOriginal = dateTimeOriginal;
	}
	public Date getDateTimeDigitized() {
		return DateTimeDigitized;
	}
	public void setDateTimeDigitized(Date dateTimeDigitized) {
		DateTimeDigitized = dateTimeDigitized;
	}
	public Date getDateTime() {
		return DateTime;
	}
	public void setDateTime(Date dateTime) {
		DateTime = dateTime;
	}
	public String getISO() {
		return ISO;
	}
	public void setISO(String iSO) {
		ISO = iSO;
	}
	public Short getFlash() {
		return Flash;
	}
	public void setFlash(Short flash) {
		Flash = flash;
	}
	public String getGPSInfo() {
		return GPSInfo;
	}
	public void setGPSInfo(String gPSInfo) {
		GPSInfo = gPSInfo;
	}
	public String getgPanoCaptureSoftware() {
		return gPanoCaptureSoftware;
	}
	public void setgPanoCaptureSoftware(String gPanoCaptureSoftware) {
		this.gPanoCaptureSoftware = gPanoCaptureSoftware;
	}
	public String getgPanoStitchingSoftware() {
		return gPanoStitchingSoftware;
	}
	public void setgPanoStitchingSoftware(String gPanoStitchingSoftware) {
		this.gPanoStitchingSoftware = gPanoStitchingSoftware;
	}
	public String getgPanoUsePanoramaViewer() {
		return gPanoUsePanoramaViewer;
	}
	public void setgPanoUsePanoramaViewer(String gPanoUsePanoramaViewer) {
		this.gPanoUsePanoramaViewer = gPanoUsePanoramaViewer;
	}
	public String getgPanoProjectionType() {
		return gPanoProjectionType;
	}
	public void setgPanoProjectionType(String gPanoProjectionType) {
		this.gPanoProjectionType = gPanoProjectionType;
	}
	public String getgPanoCroppedAreaLeftPixels() {
		return gPanoCroppedAreaLeftPixels;
	}
	public void setgPanoCroppedAreaLeftPixels(String gPanoCroppedAreaLeftPixels) {
		this.gPanoCroppedAreaLeftPixels = gPanoCroppedAreaLeftPixels;
	}
	public String getgPanoCroppedAreaTopPixels() {
		return gPanoCroppedAreaTopPixels;
	}
	public void setgPanoCroppedAreaTopPixels(String gPanoCroppedAreaTopPixels) {
		this.gPanoCroppedAreaTopPixels = gPanoCroppedAreaTopPixels;
	}
	public String getgPanoCroppedAreaImageWidthPixels() {
		return gPanoCroppedAreaImageWidthPixels;
	}
	public void setgPanoCroppedAreaImageWidthPixels(
			String gPanoCroppedAreaImageWidthPixels) {
		this.gPanoCroppedAreaImageWidthPixels = gPanoCroppedAreaImageWidthPixels;
	}
	public String getgPanoCroppedAreaImageHeightPixels() {
		return gPanoCroppedAreaImageHeightPixels;
	}
	public void setgPanoCroppedAreaImageHeightPixels(
			String gPanoCroppedAreaImageHeightPixels) {
		this.gPanoCroppedAreaImageHeightPixels = gPanoCroppedAreaImageHeightPixels;
	}
	public String getgPanoFullPanoWidthPixels() {
		return gPanoFullPanoWidthPixels;
	}
	public void setgPanoFullPanoWidthPixels(String gPanoFullPanoWidthPixels) {
		this.gPanoFullPanoWidthPixels = gPanoFullPanoWidthPixels;
	}
	public String getgPanoFullPanoHeightPixels() {
		return gPanoFullPanoHeightPixels;
	}
	public void setgPanoFullPanoHeightPixels(String gPanoFullPanoHeightPixels) {
		this.gPanoFullPanoHeightPixels = gPanoFullPanoHeightPixels;
	}
	public String getgPanoNorthPosInX() {
		return gPanoNorthPosInX;
	}
	public void setgPanoNorthPosInX(String gPanoNorthPosInX) {
		this.gPanoNorthPosInX = gPanoNorthPosInX;
	}
	public String getgPanoLargestValidInteriorRectLeft() {
		return gPanoLargestValidInteriorRectLeft;
	}
	public void setgPanoLargestValidInteriorRectLeft(
			String gPanoLargestValidInteriorRectLeft) {
		this.gPanoLargestValidInteriorRectLeft = gPanoLargestValidInteriorRectLeft;
	}
	public String getgPanoLargestValidInteriorRectTop() {
		return gPanoLargestValidInteriorRectTop;
	}
	public void setgPanoLargestValidInteriorRectTop(
			String gPanoLargestValidInteriorRectTop) {
		this.gPanoLargestValidInteriorRectTop = gPanoLargestValidInteriorRectTop;
	}
	public String getgPanoLargestValidInteriorRectWidth() {
		return gPanoLargestValidInteriorRectWidth;
	}
	public void setgPanoLargestValidInteriorRectWidth(
			String gPanoLargestValidInteriorRectWidth) {
		this.gPanoLargestValidInteriorRectWidth = gPanoLargestValidInteriorRectWidth;
	}
	public String getgPanoLargestValidInteriorRectHeight() {
		return gPanoLargestValidInteriorRectHeight;
	}
	public void setgPanoLargestValidInteriorRectHeight(
			String gPanoLargestValidInteriorRectHeight) {
		this.gPanoLargestValidInteriorRectHeight = gPanoLargestValidInteriorRectHeight;
	}
	public short getOrientation() {
		return Orientation;
	}
	public void setOrientation(short orientation) {
		Orientation = orientation;
	}	

}
