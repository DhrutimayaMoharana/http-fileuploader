//package com.https.fileuploader.entity;
//
//import java.io.Serializable;
//import java.time.LocalDateTime;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//import org.hibernate.annotations.CreationTimestamp;
//
////@Entity
////@Table(name = "command_send_details")
//public class CommandSendDetails implements Serializable {
//
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	private Long deviceId;
//
//	private String imeiNumber;
//
//	private Long eventId;
//
//	private String eventType;
//
//	private Long positionId;
//
//	private String evedenceFiles;
//
//	private String command;
//
//	private String commandRespone;
//
//	private Boolean status;
//
//	private String vehicleStatus;
//
//	private Boolean filesExistance;
//	
//	private Integer noOfFileReq;
//	
//	private Integer noOfFileUploaded;
//	
//	private String filesUploaded;
//
//	private String fileDownLoadUrl;
//
//	@CreationTimestamp
//	private LocalDateTime createOn;
//
//	private LocalDateTime reCallOn;
//
//	private Integer reCallCount;
//	
//	private String remarks;
//
//	public CommandSendDetails() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
//	
//	
//
//	
//}
