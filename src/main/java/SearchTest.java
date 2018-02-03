import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import komposten.utilities.search.IndexEntry;
import komposten.utilities.search.InvertedIndex;
import komposten.utilities.search.InvertedIndex.Indexable;
import komposten.utilities.search.SearchEngine;


public class SearchTest
{
	public static void main(String[] args)
	{
		System.out.println("******TESTING NAMES******");
		nameTest();
		System.out.println("******TESTING ARTICLES******");
		articleTest();
		System.out.println("******TESTING WIKI ARTICLES******");
		wikiTest();
		System.out.println("******TESTING WIKI ARTICLES V2******");
		wikiTest2();
	}


	private static void nameTest()
	{
		Name[] names = new Name[200];
		Random random = new Random(2l);

		for (int i = 0; i < names.length; i++)
		{
			names[i] = randomName(random);
		}

//		names = new Name[] { new Name("Hugh Brooke Pittman"),
//				new Name("Guadalupe Kayla Allen"), new Name("Esther Meredith Ramos"),
//				new Name("Sandy Lucia Brooks"), new Name("Thelma Meredith Webb"),
//				new Name("Kristopher Guadalupe Harrison"),
//				new Name("Beverly Lucia Sandoval"),
//				new Name("Guadalupe Meredith Lawson"),
//				new Name("Hugh Jasmine Anderson"), new Name("Troy Terry Pittman"),
//				new Name("Troy Troy Pittman") };

		InvertedIndex<Name> index = new InvertedIndex<Name>(names);
		
		System.out.println("Indexed objects: " + index.getIndexableCount());
		System.out.println("Indexed terms: " + index.getIndexSize());

		double[] troy = index.getTermFrequencies().get("troy");
		Double troy2 = index.getInverseDocumentFrequencies().get("troy");

		SearchEngine<Name> search = new SearchEngine<Name>(index);
		System.out.println("\nFull index:");
		printMap(index.getIndex());
		System.out.println("\nTroy:");
		printList(search.query("Troy Brooke", true, false));
		System.out.println("\nTroy~:");
		printList(search.query("Troy Brooke", false, false));
	}
	
	
	private static void articleTest()
	{
		Name[] articleNames = new Name[articles.length];
		for (int i = 0; i < articles.length; i++)
			articleNames[i] = new Name(articles[i]);

		InvertedIndex<Name> index = new InvertedIndex<Name>(articleNames);
		
		System.out.println("Indexed objects: " + index.getIndexableCount());
		System.out.println("Indexed terms: " + index.getIndexSize());

		SearchEngine<Name> search = new SearchEngine<Name>(index);
		String query = "computer";
		System.out.println("\n" + query);
		printList(search.query(query, true, false));
		System.out.println("~" + query);
		printList(search.query(query, false, false));
		
		query = "computer programming";
		System.out.println("\n" + query);
		printList(search.query(query, true, false));
		System.out.println("~" + query);
		printList(search.query(query, false, false));
		
		query = "compter programm";
		System.out.println("\n" + query);
		printList(search.query(query, true, false));
		System.out.println("~" + query);
		printList(search.query(query, false, false));
	}
	
	
	private static void wikiTest()
	{
		long time = System.nanoTime();
		System.out.println("Loading articles...");
		Name[] articleNames = loadWikiArticles();
		System.out.println("Load complete, took " + ((System.nanoTime() - time) / 1E9) + " seconds.");

		time = System.nanoTime();
		System.out.println("Creating index...");
		InvertedIndex<Name> index = new InvertedIndex<Name>(articleNames);
		System.out.println("Index built, took " + ((System.nanoTime() - time) / 1E9) + " seconds.");
		
		System.out.println("Indexed objects: " + index.getIndexableCount());
		System.out.println("Indexed terms: " + index.getIndexSize());

		SearchEngine<Name> search = new SearchEngine<Name>(index);
		String query = "test machine";
		time = System.nanoTime();
		System.out.println("\n" + query);
		List<Name> searchResult = search.query(query, true, false);
		System.out.println("Search performed, took " + ((System.nanoTime() - time) / 1E9) + " seconds.");
		printList(searchResult);
		time = System.nanoTime();
		System.out.println("~" + query);
		searchResult = search.query(query, false, false);
		System.out.println("Search performed, took " + ((System.nanoTime() - time) / 1E9) + " seconds.");
		printList(searchResult);
	}
	
	
	private static void wikiTest2()
	{
		long time = System.nanoTime();
		System.out.println("Loading articles...");
		Name[] articleNames = loadWikiArticles2();
		System.out.println("Load complete, took " + ((System.nanoTime() - time) / 1E9) + " seconds.");

		time = System.nanoTime();
		System.out.println("Creating index...");
		InvertedIndex<Name> index = new InvertedIndex<Name>(articleNames);
		System.out.println("Index built, took " + ((System.nanoTime() - time) / 1E9) + " seconds.");
		
		System.out.println("Indexed objects: " + index.getIndexableCount());
		System.out.println("Indexed terms: " + index.getIndexSize());

		SearchEngine<Name> search = new SearchEngine<Name>(index);
		String query = "great britain";
		time = System.nanoTime();
		System.out.println("\n" + query);
		List<Name> searchResult = search.query(query, true, false);
		System.out.println("Search performed, took " + ((System.nanoTime() - time) / 1E9) + " seconds.");
		printList(searchResult);
		time = System.nanoTime();
		System.out.println("~" + query);
		searchResult = search.query(query, false, false);
		System.out.println("Search performed, took " + ((System.nanoTime() - time) / 1E9) + " seconds.");
		printList(searchResult);
	}


	private static void printMap(Map<String, ArrayList<IndexEntry<Name>>> map)
	{
		for (Entry<String, ArrayList<IndexEntry<Name>>> entry : map.entrySet())
		{
			System.out.print(entry.getKey() + ", " + entry.getValue().size() + ": ");
			for (IndexEntry<Name> indexEntry : entry.getValue())
			{
				System.out.print(indexEntry.getIndexable().getText() + "{");
				for (int i : indexEntry.getTermPositions())
					System.out.print(i + ", ");
				System.out.print("}, ");
			}
			System.out.println();
		}
	}


	private static void printList(List<Name> query)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int counter = 0;
		for (Indexable indexable : query)
		{
			String text = indexable.getText();
			
			if (text.length() > 30)
			{
				text = text.substring(0, 30) + "...";
			}
			
			builder.append(text + ", ");
			
			if (counter++ > 10)
			{
				builder.append("...");
				break;
			}
		}
		builder.append("]\n");
		System.out.println(builder.toString());
	}


	private static Name randomName(Random random)
	{
		String[] firstNames = new String[] { "Kristopher", "Troy", "Esther",
				"Kayla", "Brooke", "Alberto", "Thelma", "Lauren", "Perry", "Guadalupe",
				"Zachary", "Beverly", "Van", "Sandy", "Lynda", "Lucia", "Geraldine",
				"Alonzo", "Hugh", "Meredith", "Alberta", "Jasmine", "Terry", "Toni",
				"Winston" };
		String[] lastNames = new String[] { "Boyd", "Manning", "Ramos", "Sandoval",
				"Gregory", "Pittman", "Henderson", "Leonard", "Drake", "Erickson",
				"Adkins", "Gonzalez", "Allen", "Thomas", "Hill", "Anderson", "Lawson",
				"Harrison", "Ramsey", "Smith", "Obrien", "Brooks", "Nunez", "Webb",
				"Benson" };

		String firstName = firstNames[random.nextInt(firstNames.length)];
		String middleName = firstNames[random.nextInt(firstNames.length)];
		String lastName = lastNames[random.nextInt(lastNames.length)];

		return new Name(firstName + " " + middleName + " " + lastName);
	}
	
	
	private static Name[] loadWikiArticles()
	{
		ArrayList<Name> names = new ArrayList<SearchTest.Name>();
		
		File file = new File("C:\\Users\\Jakob\\Desktop\\Programmering\\Projekt\\Utilities\\Inverted index\\tfidf_code\\testCollection.dat");
		BufferedReader scanner;
		
		try
		{
			scanner = new BufferedReader(new FileReader(file));
			StringBuilder builder = new StringBuilder();
			boolean isInBody = false;
			
			
			String line = scanner.readLine().trim();
			
			while (line != null)
			{
				line = line.trim();
				
				if (line.matches("<title>.+</title>"))
				{
					builder.append(line.substring(7, line.length() - 8));
					String string = builder.toString().replaceAll("[^A-Za-z0-9_]", " ");
					names.add(new Name(string));
					builder = new StringBuilder();
				}
//				else if (line.contains("<text>"))
//				{
//					isInBody = true;
//					builder.append(line.replaceAll("<text>", ""));
//				}
//				else if (line.contains("</text>"))
//				{
//					isInBody = false;
//
//					builder.append(line.replaceAll("</text>", ""));
//					String string = builder.toString().replaceAll("[^A-Za-z0-9_]", " ");
//					names.add(new Name(string));
//					builder = new StringBuilder();
//				}
//				else if (isInBody)
//				{
//					builder.append(" " + line);
//				}

				line = scanner.readLine();
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return names.toArray(new Name[0]);
	}
	
	
	private static Name[] loadWikiArticles2()
	{
		ArrayList<Name> names = new ArrayList<SearchTest.Name>();
		File file = new File("C:\\Users\\Jakob\\Desktop\\enwiki-20180120-all-titles\\enwiki-20180120-all-titles-only-titles.txt");
		BufferedReader scanner;
		
		try
		{
			scanner = new BufferedReader(new FileReader(file));
			String line = scanner.readLine();
			
			int i = 0;
			while (line != null && i++ < 100000)
			{
				line = line.trim();
				
				names.add(new Name(line.replaceAll("[^A-Za-z0-9_]", "")));
				
				line = scanner.readLine();
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return names.toArray(new Name[names.size()]);
	}


	private static class Name implements Indexable
	{
		private String name;


		public Name(String name)
		{
			this.name = name;
		}


		@Override
		public String getText()
		{
			return name;
		}
	}
	
	
	private static final String[] articles = new String[] {
			"Some Opinions on Leaving Your Computer On 24 Hours a Day and Mounting Your Machine Sideways, 1990",
			"On the Evolution of Unix and the Automation of Telephone Support Operations by Ronda Hauben",
			"Three Dimensional Rotations for Computer Graphics, by Lithium of VLA",
			"Three Dimensional Shading in Computer Graphics by Lthium of VLA",
			"4DOS Specific Information and Tips",
			"A Memo on the Secret Features of 6309",
			"Information about the 6502 taken from a 6510 Commodore 64 Manual",
			"Information on 6502 Bugs, from Ivo van Poorten (November 2, 1994)",
			"Learning Assembly in One Stp from RTK (July 23rd, 1997)",
			"A Proposed Assembly Language Syntax For 65c816 Assemblers by Randall Hyde",
			"The 65C816 Dream Machine", "Summary of 8080 Instructions",
			"Harald Feldmann's 86BUGS List (November 3, 1994)",
			"The American Animation Institute 1987 Class Schedule (WHAT?)",
			"Why the Hell Would You Learn Programming?",
			"ACRONYMS (and things that look like them)",
			"DOCUMENTATION: The Cyberpunk's Address Book v1.01 by Robert D. Bouman (February 4, 1994)",
			"Understanding Effective Addresses in Assembly",
			"Advanced 6502 Assembly Code Examples by M.J. Malone",
			"Adventure: More Parsing, by Bob Wiber",
			"Checkpoint Codes for Pre April 1990 American Megatrends BIOS Chips",
			"How to Make ANSI by Def Leppard",
			"Using the ANSI Driver by C. Scot Giles", "ANSI Codes Demystified",
			"ANSI Codes that COM: Responds to",
			"Anti-Debugging Tricks by Inbar Raz",
			"Release 1 of the Anti-Anti Debugging Tricks Article",
			"The Commodore ARC Format: A Description",
			"BBS Messages: Archives Sub Board, Magpie",
			"The Commodore Arkive Format: An Overview",
			"Skysurfing the 'Net by Bradley C. Spatz (January 15, 1995)",
			"Thoughts about Making Graphics Look Good",
			"Representing IPA Transcriptions in ASCII",
			"The Captain's Log ASCII List", "An ASCII Code Table",
			"ASCII Codes for the TRS Model 100",
			"IBM PC Assembly Tutorial, by Joshua Auerbach",
			"IBM Personal Computer Assembly Language Tutorial by Joshua Auerbach, Yale University",
			"Assembly Programming Tutorial",
			"An Assembly Language Tutorial for the Radio Shack Model 100 by Mike Berro (1984)",
			"The VGA Trainer Program by Denthor of Asphyxia (1994)",
			"VERY Large Collection of AST Technical Bulletins from 1991-1993",
			"FAQ: AT&T UNIX PC",
			"Character Attributes on Video Boards for the IBM PC",
			"FAQ: MS-DOS Programmers Frequently Asked Questions (December 17, 1995)",
			"Simple Way to Detect Game Wizard!",
			"BABEL: A Listing of Computer-Oriented Abbreviations and Acronyms, by Irving Kind (1991)",
			"BABEL: A Listing of Computer Oriented Abbreviations and Acronyms",
			"AWK as a C Code Generator by Wahhab Baldwin",
			"How a Ballistics Program Works (Or How to Calculate a Trajectory Chart) by R. White",
			"A Treatise on the Efficient and Elegant Use of Basic on the TRS-80 Model 100 by Richard Horowitz",
			"Batch and Configuration Files in DOS: Some Tips and Tricks",
			"Good Solid UNIX Tutorial", "C Procedure Tables by Tim Berens",
			"Programmer's Technical Reference for MSDOS and the IBM PC by Dave Williams (January 12, 1992)",
			"Bio-Control by Neural Networks: Summary of a Workshop by the NSF (May 16-18, 1990)",
			"Some BIOS and Motherboard Specific Information for PC Compatibles",
			"AMI 286 and 386 BIOS Release Notes (September 25, 1988)",
			"A BIOS Diagnosis Manual", "List of Available AMI BIOS Products (1988)",
			"A Project Board Test Program in 6502 Assembly by M.J. Malone",
			"User Bourne Shell Programming, a Manual (February 7, 1991)",
			"SUMMARY PAPER: An Architectural Overview of UNIX Network Security",
			"The Nag-Buster (Documentation) from Erik Famm(1994)",
			"C Made Easier Lesson #4 from the Not (January 15, 1991)",
			"C Programming Series Issue #1 by Pazuzu *April 21, 1993)",
			"The 10 Commandments for C Programmers by Henry Spencer",
			"A Quick Guide to Computer Cables by Bill becwar",
			"Living With DOS: Disk Caches by Barry Simon",
			"A list of Patents Referring to Cellular Automata",
			"The Line Signals in CCITT Systems",
			"A Noah's Ark Program, by Rudy Rucker",
			"The IBM PC Programmer's Guide to C 3rd Edition by Matthew Probert",
			"CIS Threads #1: Interesting Threads from the TRS Model 100 Forum Messages (1987) by Phil Wheeler",
			"The CMOS Memory Map v1.23 (June 1994) by Padgett Peterson",
			"Compressing Your Executables: Something for Nothing? By Dickford Cohn",
			"Every Digital Computer Type Ever Made, 1992",
			"List of the world's most powerful computing sites as of 11-JAN-1993",
			"Comrap's Guide to Remote Database Systems for New Users",
			"Getting Started as a Computer Consultant",
			"Everything You Wanted to know about Math Coprocessors",
			"Hacking Away at the Counterculture by Andrew Ross",
			"A Computer and Information Technologies Platform",
			"Modem Protocol Proposal, CRC Extension",
			"A Painless Guide to CRC Error Detection Alhorithms, by Ross N. Williams (August 19, 1993)",
			"Jack Crenshaw's Tutorial on Compiler Consuction 1-15 (1989, Re-Edited 1994)",
			"Crash Protecting GBBS II", "CSH Programming is Considered Harmful",
			"An introduction to the C shell by William Joy (Needs Editing)",
			"The Trick of the Clockwise/Spiral Rule by David Anderson (1994)",
			"Information on the DOS CTTY Program, by Dan Derrick (February 21, 1984)",
			"Manifesto of the Cyberpunks by Wyze1",
			"Compatibility issues Cyrix Cx486SLC/DLC as compared to the Intel 80486SX",
			"Performance Comparison Intel 386DX, Intel RapidCAD, C&T 38600DX, Cyrix 486DLC",
			"DDT: A Manual for DDT by ITS HACTRN (1990)",
			"A Tutorial on the Use of DEBUG on the IBM PC",
			"Introduction to using the DECSYSTEM-20 by Dundee College of Technology",
			"Demos - only for Europe? / Celebrandil of PHA",
			"Suggested Programming Style and Debugging Methods Using DR6502 by M.J. Malone",
			"Listing of IBM PC Diagnostic Error Codes",
			"An Introduction to Troubleshooting Your IBM PC by Jerry Schneider",
			"A Collection of Disk Myths for Apple II Disks",
			"How to Program the DMA by Night Stalker of Rage Technologies, Inc.",
			"An Introduction to DMA by Draeden of VLA",
			"Application of Virtual Reality to Weapon System Concept Evaluation in a Distributed Simulation Environment (July, 1992)",
			"DOS 6: The Real Story, by The Brother-In-Law",
			"DOS Technical Information Manual/Programming Technical Reference, by Dave Williams",
			"Documentation for DR6502: A 6502 Software and Hardware Execution Simulator System bu M.J. Malone",
			"Supplementary Notes about DR6502 by M.J. Malone",
			"The DHRYSTONE Benchmark Program Source Code (January 6, 1986)",
			"Documentation for DTS/DTR Test Programs",
			"The Easter Egg Hunt Results (Computer Easter Eggs) by Joe Morris (July 12, 1993)",
			"Summary of Text Editor Features",
			"E.D.O.E a Unified View of Computing by T. Dunlop",
			"IBM Enhanced Graphics Adapter EGA BIOS Interface Routines",
			"Extended Graphics Adaptor Technical Info",
			"Danny Cohen's Article in which he popularized the terms 'Big Endian' and 'Little Endian'",
			"Enhancing PC Disk Performance by Bruce Schafer",
			"Secure Distributed Databases for Epidemiological Control by D.S. Stodolsky (October 29, 1989)",
			"Neural Nets Improve Hospital Treatment Quality and Reduce Expenses, by Jeannette Lawrence",
			"A Whole Bunch of Theoretical Equations by Erik Von Francis",
			"A Comment on Error Traps",
			"The Professional Code of Ethics of the International Programmers' Guide",
			"Virus On Computer Disks Spurs Elek-Tek to Order Recall, by Wilma Randle (January 18, 1992)",
			"The Care and Feeding of Foreign Characters by Alex Gross (1991)",
			"6502 Assembly Code Examples by M.J. Malone",
			"Additional Examples of 6502 Assembly Code by M.J. Malone",
			"Fast Lane Modems (14.4k! YEAH!)",
			"How to use Fixed Point (16.16) Math (Part 1 of 2), by Night Stalker (March 12, 1995)",
			"How to use Fixed Point (16.16) Math (Part 2 of 2), by Night Stalker (March 12, 1995)",
			"Flight Simulator RGB Modifications by Andrew Tuline",
			"Foolproof and the Subsequent Destruction Thus Thereof",
			"The Story of GOLEM XIV: The Most Incredible Computer",
			"A Collection of Interesting Formulas of use to Computer Programmers",
			"Bibliography of Material on Floating Point Arithmetic",
			"Messages Discussing the Weirdness of the Stage.Dat File on Prodigy (1991)",
			"Interview and Overview of Richard Stallman and the Free Software Foundation (1993)",
			"A Quick Explanation of Fuzzy Logic",
			"An Introduction to Careers in Computer Games by Bill Armintrout (August 8, 1994)",
			"Power Glove Gesture Recognition (Nintendo Power Glove)",
			"The Compuserve GIF89a Specification Programming Reference",
			"What is GIF? By Compuserve, May 28, 1987",
			"The Mathematics of Three-Dimensional Manipulations and Transformations by Trip V. Reece (June 1992)",
			"USENET Postings About Hooking Nintendo Powergloves to Atari STs (October 12, 1991)",
			"A Glossary for the Intel 386",
			"A General Overview on Suggested Standards for GNU Coding",
			"MSDOS file for the GNUish MSDOS project by the Free Software Foundation (January 30, 1992)",
			"Using a Printer Port for Simple GPIB/IEEE-488 Operation by Sydex (1990)",
			"Discussions about Gravity and Accurate Jumping",
			"Nationwide Ground To Air Stations",
			"The Programmer's Guide by Nelson Ford (January 1989)",
			"A Programmer's Guide, by Nelson Ford (1991)",
			"Hacking Quickmail for Macintosh, by the Brighter Buccaneer (February 28, 1995)",
			"Welcome to the Adventures of Earl! By Mke Graham (April 20th, 1991)",
			"Model 100 Hints and Tips",
			"PopDBF: Instant Access to X-Base Files, Verison 4.0 Users' Manual",
			"xBase Identifier Naming Conventions or HUNG: Hungarian Types Without the Arian, by Robert A DiFalco",
			"The IBM PC Special Characters", "IBM PC BIOS Service Codes",
			"Cracking on the IBM PC, by Buckaroo Banzai",
			"Solution to the Fooblitzky Conspiracy",
			"The Linux Information Sheet (March 17, 1993)",
			"The Intercal Programming Language Reference Manual by Donald R. Woods and James Lyon (1973) (1990)",
			"UNIX For Intermediate Users",
			"Interrupt Structure, by Janet Jack, J. Weaver Jr., John Cooper, Skip Gilbrech, Jim Kyle and Bob Jack (1985)",
			"The International Programmers' Guild Professional Certification and Recognition for Programmers",
			"KRACK, the official cracking textfile, by the Cellar Elite (April 1986)",
			"An Introduction to the NEC PC8201a & PC8300 by Ron Hopkins-Lutz (October, 1994)",
			"The Library Format: An Overview",
			"RAM Memory Locations and Values for TRS-80 Model 100 Computers by Tony Anderson (1984)",
			"The M100 Hackers' Guide by Andy Diller (1996)",
			"The TRS-80 Model 100 Quick Reference Guide from ACD (January, 1996)",
			"Tandy 200 RAM Creation by Ken Nickerson",
			"The Tandy Model 200 Telcom Manual",
			"A T200/100 to IBM PC File Exchange from Kent Nickerson",
			"Document for Matrix Toolbox Function Calls, by Patrick Ko Shu Pui",
			"Some Examples of Programs that Produce Mazes",
			"Quick Overview of common Memory Errors on IBM-Compatible Systems",
			"The Usenet MIDI Primer",
			"Mastering Milliways Part I: Hard Drive Partitions",
			"Error Correction in Modems, and the MNP Protocol by Greag Pearson, Developer of MNP",
			"The TRS-80 Model 100 Page",
			"MODEM Specific Information: Tips and Tricks",
			"More 6502 Assembly Code Examples by M.J. Malone",
			"Apple Mouse Programming",
			"Some MS-DOS Specific Information: Tips and Tricks",
			"Helpful Primer on Creating Music with ANSI Codes",
			"Overview of a couple Natural Language Programs",
			"How to Use a Buffering FIFO Queue to Output your Graphics, from Imphobia",
			"DOCUMENTATION: Netcat Verison 1.10",
			"The American Radio Relay League Newsletter #21",
			"The Modem Noise Killer, Alpha Version",
			"6502 Undocumented Opcodes Based on the Atari 8-bit 6502 Version 3.0, byFreddy offenga (May 17, 1997)",
			"WD65C816 Opcode Reference by Eric D. Shepherd (February 20, 1993)",
			"Some Information on Optimizing Code on a 386, 486 or Pentium, by Michael Kunstelj",
			"Brief Description of the 256 Interripts (June 5, 1994)",
			"Towards Reducing the Hardware Complexity of Feature Detection-based Models (Neural Networks) by Bassem Medawar and Andrew Noetzell",
			"The PC Games Programmers' Encyclopedia Version 1.0",
			"Recommendations Report for File Transfer via PC Pursuit (October 17, 1988)",
			"Large List of PEEK and POKE Locations for the IBM",
			"A Poker Hands List. Yeah, perfect for the Programming Section",
			"XT, AT, and PS/2 I/O Port Addresses by Wim Osterhold (August 7, 1994)",
			"REVIEW: Procomm: Outstanding Telecomm by Merv Adrian",
			"PROCOMM: Outstanding Telecomm by Merv Adrian (Part II)",
			"Protocol Notes: CIS A Protocol",
			"Introduction to Windows Programming for MS-DOS Programmers",
			"QuickBASIC Tutorial: Make a PACMAN Move Across the Screen!",
			"QuickBASIC Tutorial: Make an Role Playing Game",
			"Quick/Kick Start into DR6502 Assembly Language Programming, by M.J. Malone",
			"FORMAT: QWK Mail Packet File Layout by Patrick Y. Lee (1992)",
			"QWK Mail Packet File Layout, by Patrick Y Lee",
			"Packet Radio Links for the EASTNET8 Network",
			"Computer Generated Random Numbers by David W. Deley (1991)",
			"Public Domain/Freeware/Shareware by Ralf Brown (A List)",
			"Differences between DecNET DOS Version 1.0 and 1.1",
			"Slams the Hayes 2400 Baud Modem, lauds the US Robotics 2400",
			"Creating a Robotic Sounding Voice",
			"Understanding Reverse-Polish Notation",
			"A Generic Heapsort Algorithm in C by Stephen Russell",
			"The S-Buffer FAQ, by Paul Nettle Version 1.0.0 (March 10, 1995)",
			"Santa Cruz Organization Special License for Ancient Unix Source",
			"DOCUMENTAITON: SecureDrive v1.3c",
			"Improving the Security of your UNIX System by David Curry, 1990",
			"A Brief History of UNIX Shells, as well as a coimparison chart between them",
			"Review of ShowRIP v3.01 by Chuck Warrix", "What IS Shareware?",
			"Signalling Systems Around the World, from TAP and Nick Haflinger",
			"The Smallification Wars? Where will the Compression End? by Jerry Weichbrodt",
			"DOCUMENTATION: SoftICE (PC Assembly Disassembler)",
			"Techniques of Computer Bubble Sorts",
			"Prototyping SP4: A Secure Data Network System Transport Protocol (National Computer Systems Laboratory)",
			"A Method of Sprite Animation, by Darren Gyles",
			"What's the Difference Between the BE3011 and SmartROM?",
			"Starting to Program in 6502 Assembly Code by M.J. Malone",
			"Scorpio's Tips and Tricks Information Files",
			"Inside Sun Raster, by Jamie Zawinksi",
			"Some (S)VGA Tricks, from Imphobia",
			"Seven Year Test Results: The Tandy 102 Reduces Levels of Portable Computer Anxiety and Violence by Bick Truet, Senior Partner, Technologies Research Group",
			"Full List of Terminal Identifiers", "The Ten Commandments of RBBS",
			"Nikola Tesla's Long Range Weapon by Oliver Nichelson",
			"Inside Turbo Pascal 5.5. Units, by William L. Peavy (August 11, 1990)",
			"Troubleshooting the 6502 Project Board by M.J. Malone",
			"Converting a 3D Shape into Triangles",
			"Large List of Easter Eggs and Secret Messages in a lot of Computers and Products",
			"Some 6502 Tricks and Tips from M.J. Malone",
			"NCSL Bulletin: Advising users on computer systems technology, July 1990",
			"Undocumented features of TRS-80 Model 100 BASIC",
			"How to install 640k of RAM on a IBM XT motherboard",
			"The VAX Programs List (1990)", "VBBS Heart-Code ANSI Color Chart",
			"A Comparative Study of VI and Emacs from the Perspective of Novice and Regular Users, by William Knottenbelt",
			"Companies that Manufacture VR Systems or Components",
			"The Wordperfect 5.1 Bedtime Story (September 10, 1989)",
			"How to Program: A Tutorial", "Who's Who in Shareware, November 1994",
			"List of Games for the X11 Graphics System, 18th August 1994",
			"DOCUMENTATION: [XiT] v2.0 by Roche' Crypt (February, 1994)",
			"The XMODEM File Transfer Protocol, by Larry Jordan",
			"Intrinsics of the X Toolkit by Todd Lainhart",
			"Information on X-Windows Terminal Emulation for PC/AT Clones by Gerolf Starke (November 19, 1991)",
			"Generating Parsers with PCYACC by Alex Lane (1989)",
			"XMODEM/YMODEM Protocol Reference by Chuck Forsberg",
			"Inmark zApp vs. Borland Object Windows Library",
			"An Evolutionary Approach to Synthetic Biology: Zen and the Art of Creating Life by Thomas S. Ray (October 21, 1993)",
			"MESSAGES: A Warning on PKZIP Trojan (May 15, 1989)",
			"The ZMODEM Inter Application File Transfer Protocol, by Chuck Forsberg" };
}
