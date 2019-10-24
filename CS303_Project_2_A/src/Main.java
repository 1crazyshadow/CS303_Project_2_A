import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * @title: CS303 - Project 2A
 * @author: Collin Thomason
 *
 * For path, do not include file/folder name
 */
public class Main {
    private static TreeMap<String, MyProjectClass> directories = new TreeMap<>();

    public static void main ( String args[] ) {
        try {
            addFolder( "", "C:" );
            addFolder( "C:", "home" );
            addFile( "C:\\home", "game", 20 );
            addFile( "C:\\home", "temp", 5 );
            deleteFile( "C:\\home", "temp" );
            addFolder( "C:\\home", "school" );
            addFolder( "C:\\home\\school", "documents" );
            addFolder( "C:\\home\\school", "cs303" );
            addFile( "C:\\home\\school", "umkcPhoto", 8 );
            addFile( "C:\\home\\school\\documents", "resume", 7 );
            addFile( "C:\\home\\school\\cs303", "project 2", 35 );
            deleteFolder( "C:\\home\\school", "cs303" );
            List<File> files = getFiles( "C:", "resume" );
            for ( File file: files ) {
                System.out.println( file.getName() );
            }
        } catch ( Exception e ) {
            System.out.println( e.getMessage() );
        }
    }

    /**
     * Adds a folder to the directories
     * @param path path the folder will have
     * @param folderName
     * @throws Exception when the path to the folder does not exist
     */
    private static void addFolder( String path, String folderName ) throws Exception {
        if ( directories.containsKey( path ) || path.equals( "" ) ) {
            Folder folder = new Folder();
            folder.setName( folderName );
            folder.setSize( 0 );
            String fullPath;
            if ( !path.equals( "" ) ) {
                fullPath = path + "\\" + folderName;
            }
            else {
                fullPath = folderName;
            }
            directories.put( fullPath, folder );
        }
        else {
            throw new Exception( "Path does not exist" );
        }
    }

    /**
     * Deletes the given folder in the given path
     * @param path the path to the given folder (does not include folder being deleted)
     * @param folderName the name of the folder in the path to delete
     * @throws Exception when the folder does not exist on the given path
     */
    private static void deleteFolder( String path, String folderName ) throws Exception {
        String fullPath = path + "\\" + folderName;
        if ( directories.containsKey( fullPath ) ) {
            ArrayList<String> keys = new ArrayList<>(); // Will contains keys of child folders and files to remove
            Iterator<String> keyIterator = directories.keySet().iterator();

            // Finds the keys of files and folders to delete
            for( int i = 0; i < directories.keySet().size(); i++ ) {
                String key = keyIterator.next();
                if ( key.contains( fullPath ) ) {
                    keys.add( key );
                }
            }

            // Changes size of all parent folders
            Folder folder = (Folder) directories.get( fullPath );
            changeSize( path, -folder.getSize() );

            // Removes all child folders and files
            for ( String key: keys ) {
                directories.remove( key );
            }
        }
        else {
            throw new Exception( "Path does not exist" );
        }
    }

    /**
     * Adds a file to the given directory
     * @param path the path to where the file will be added
     * @param fileName the name of the file to add
     * @param size the size of the file being added
     * @throws Exception when the path does not exist in the tree
     */
    private static void addFile( String path, String fileName, int size ) throws Exception {
        if ( directories.containsKey( path ) ) {
            // Add File
            File file = new File();
            file.setName( fileName );
            file.setSize( size );
            String fullPath = path + "\\" + fileName;
            directories.put( fullPath, file );

            // Change size in folder
            changeSize( path, size );
        }
        else {
            throw new Exception( "Path does not exist" );
        }
    }

    /**
     * Returns a list of all files inside the given directory that contain fileName
     * @param path The path to where to check for the files
     * @param fileName what the files returned should contain within their name
     * @return a list of files with a name containing fileName
     * @throws Exception when the path does not exist in directories
     */
    private static List<File> getFiles( String path, String fileName ) throws Exception {
        if ( directories.containsKey( path ) ) {
            ArrayList<String> keys = new ArrayList<>();
            ArrayList<File> files = new ArrayList<>();
            Iterator<String> keyIterator = directories.keySet().iterator();

            for( int i = 0; i < directories.keySet().size(); i++ ) {
                String key = keyIterator.next();
                if ( key.contains( path ) && directories.get( key ).getClass() == File.class && directories.get( key ).getName().contains( fileName ) ) {
                    keys.add( key );
                }
            }

            for ( String key: keys) {
                files.add( (File) directories.get( key ) );
            }

            return files;
        }
        else {
            throw new Exception( "Path does not exist" );
        }
    }

    /**
     * Deletes the given file in the given file path
     * @param path the path to get to the file
     * @param fileName name of the file to delete
     * @throws Exception when the directory for the file does not exist
     */
    private static void deleteFile( String path, String fileName ) throws Exception {
        if ( directories.containsKey( path ) ) {
            String fullPath = path + "\\" + fileName;

            // Subtract the file's size from the folder
            File file = (File) directories.get( fullPath );
            changeSize( path, -file.getSize() );

            // Remove the file
            directories.remove( fullPath );
        }
        else {
            throw new Exception( "Path does not exist" );
        }
    }

    // TODO : Change size method? For deleteFolder, addFile, and deleteFile

    /**
     * Changes the size of all folders in the given path
     * @param startingPath folder to start with
     * @param size size to add or subtract from folders in path, positive int to add, negative int to subtract
     */
    private static void changeSize( String startingPath, int size ) {
        int firstOccurrenceIndex;
        int lastOccurrenceIndex;
        String currentPath = startingPath;
        do {
            Folder folder = (Folder) directories.get( currentPath );
            folder.setSize( folder.getSize() + size );
            directories.replace( currentPath, folder );

            lastOccurrenceIndex = currentPath.lastIndexOf( "\\" );
            if (  lastOccurrenceIndex != -1 ) {
                currentPath = currentPath.substring( 0, lastOccurrenceIndex );
            }
        } while ( lastOccurrenceIndex != -1 );
    }
}
