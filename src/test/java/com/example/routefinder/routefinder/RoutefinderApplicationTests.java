package com.example.routefinder.routefinder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.jdbc.Sql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import com.example.routefinder.routefinder.models.Station;
import com.example.routefinder.routefinder.services.StationService;
import com.example.routefinder.routefinder.utils.ReadFileUtils;
import com.google.gson.JsonArray;

@SpringBootTest
public class RoutefinderApplicationTests {

	@Test
	public void getIdFromAliasTest() throws SQLException{
		Connection conn = null;
		conn = DriverManager.getConnection("jdbc:mysql://localhost/station_db_test?" +"user=root&password=root");

		assertEquals(0, StationService.getIdFromAlias("", conn));
		assertEquals(-1, StationService.getIdFromAlias("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", conn));
		assertEquals(1, StationService.getIdFromAlias("Paris Nord", conn));
		assertEquals(2, StationService.getIdFromAlias("Part-Dieu", conn));

	}

	@Test
	public void getConnectionFromIdTest() throws SQLException {
		
		Connection conn = null;
		List<String> resultConnectionTest;
		List<String> assertString = new ArrayList<>();
		assertString.add("Lyon Part-Dieu");
		assertString.add("Marseille Saint-Charles");
		conn = DriverManager.getConnection("jdbc:mysql://localhost/station_db_test?" +"user=root&password=root");
		resultConnectionTest = StationService.getConnectionFromId(1, conn);
		
		assertEquals(null, StationService.getConnectionFromId(-1, conn));
		assert(resultConnectionTest instanceof List<String>);
		assertEquals(assertString, resultConnectionTest);
	} 

	@Test
	public void treatJsonArrayTest() throws Exception {
		List<Station> stationsList;
		String stringJsonAssert = "[Station [id=null, stationCode=\"ACA\", stationName=\"Acapulco\", aliases=[acapulquito, La quebrada, papagayos, ACA, diamante, acapulco, guerrero, Acapulco, Playa, Arena, Beach, Outdoors, Naturaleza, Night life, Vida nocturna], connexions=[AGU, BJX, BOG, CEN, CJS, CLQ, CUL, CUN, CUU, CZM, DFW, DGO, EWR, FAT, GDL, GUA, HMO, HUX, IAH, JFK, LAP, LAS, LAX, LIM, LMM, LTO, MCO, MEX, MID, MLM, MTY, MXL, MZT, NLU, OAK, OAX, ONT, ORD, PBC, PDX, PHX, PVR, PXM, QRO, SAT, SEA, SJD, SJO, SLP, SMF, TAP, TGZ, TIJ, TJX, TPQ, TRC, VER, ZIH]], Station [id=null, stationCode=\"AGU\", stationName=\"Aguascalientes\", aliases=[AGU, Aguascalientes, hidrocalidos, Outdoors, Naturaleza], connexions=[ACA, BJX, CEN, CJS, CLQ, CUL, CUN, CUU, DEN, DGO, GDL, HMO, LAP, LMM, LTO, MEX, MLM, MTY, MZT, OAX, PBC, PHX, PVR, QRO, SJD, SLP, TAP, TGZ, TIJ, TJX, TLC, TPQ, TRC, UPN, VER, ZCL, ZIH]], Station [id=null, stationCode=\"ALB\", stationName=\"Albany\", aliases=[Albany, New, York], connexions=[]]]";
		String filePathTest = "src\\test\\java\\com\\example\\routefinder\\routefinder\\resources\\internalDataTest.json";
        JsonArray content = ReadFileUtils.readFile(filePathTest);
		stationsList =  StationService.treatJsonArray(content);
		assertEquals(stringJsonAssert, stationsList.toString());
	}

	@Test
	public void getRoutesFromStationTest() throws Exception{
				Connection conn = null;

		String originStation = "Gare du Nord";
		String resultAssert = "[Lyon Part-Dieu, Marseille Saint-Charles]";
		List<String> connectionRoutes = new ArrayList<>();
				conn = DriverManager.getConnection("jdbc:mysql://localhost/station_db_test?" +"user=root&password=root");

		connectionRoutes = StationService.getRoutesFromStation(originStation,conn);
		assertEquals(resultAssert, connectionRoutes.toString());
	}

	@Test
	public void publishStationTest() throws Exception{
		List<Station> stationsList;
						Connection conn = null;
				conn = DriverManager.getConnection("jdbc:mysql://localhost/station_db_test?" +"user=root&password=root");

		String filePathTest = "src\\test\\java\\com\\example\\routefinder\\routefinder\\resources\\internalDataTest.json";
        JsonArray content = ReadFileUtils.readFile(filePathTest);
		stationsList =  StationService.treatJsonArray(content);
		StationService.publishStation(stationsList,conn);
	}
}