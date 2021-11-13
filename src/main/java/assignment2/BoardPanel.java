package assignment2;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class BoardPanel extends GridPane implements EventHandler<ActionEvent> {

    private final View view;
    private final Board board;
    private Cell currentSelect;
    

    /**
     * Constructs a new GridPane that contains a Cell for each position in the board
     *
     * Contains default alignment and styles which can be modified
     * @param view
     * @param board
     */
    public BoardPanel(View view, Board board) {
        this.view = view;
        this.board = board;
        this.currentSelect = null;

        // Can modify styling
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #181a1b;");
        int size = 550;
        this.setPrefSize(size, size);
        this.setMinSize(size, size);
        this.setMaxSize(size, size);

        setupBoard();
        updateCells();
    }
    
    public void changeCurrectSelect() {
    	this.currentSelect = null;
    }
    public Cell getCurrentSelect() {
    	return this.currentSelect;
    }


    /**
     * Setup the BoardPanel with Cells
     */
    private void setupBoard(){ // TODO
    	
    	List<Cell> hi = board.getAllCells();

    	for (Cell cell: hi) {
    		this.add(cell, cell.getCoordinate().col, cell.getCoordinate().row);
    		cell.setDisable(false);
    		cell.setOnAction(e -> handle(e));
    		this.setHalignment(cell, HPos.CENTER);

    		
    		
    			
    		}
    		
    	}

    

    /**
     * Updates the BoardPanel to represent the board with the latest information
     *
     * If it's a computer move: disable all cells and disable all game controls in view
     *
     * If it's a human player turn and they are picking a piece to move:
     *      - disable all cells
     *      - enable cells containing valid pieces that the player can move
     * If it's a human player turn and they have picked a piece to move:
     *      - disable all cells
     *      - enable cells containing other valid pieces the player can move
     *      - enable cells containing the possible destinations for the currently selected piece
     *
     * If the game is over:
     *      - update view.messageLabel with the winner ('MUSKETEER' or 'GUARD')
     *      - disable all cells
     */
    protected void updateCells(){ // TODO
    	this.disableAllCells();
   
    	if ((view.model.getBoard().getTurn() == Piece.Type.MUSKETEER) && (view.model.getMusketeerAgent() instanceof HumanAgent) ){
	    	for (Cell i: view.model.getBoard().getPossibleCells()) {
	    		//if (view.model.getBoard().getPossibleCells().contains(i)) { 
	    			i.setDisable(false);
	    		//}
	    		
	    		if (currentSelect != null) {
	    			for (Cell ii: view.model.getBoard().getPossibleDestinations(currentSelect)) {
	    				ii.setDisable(false);
	    				this.currentSelect.setDisable(true);
	    			
	    			}
	    			
	    		}
	    	}
    	}
	    		
    		
    	
    	
    	if ((view.model.getBoard().getTurn() == Piece.Type.GUARD) && (view.model.getGuardAgent() instanceof HumanAgent)) {
	    		for (Cell i: view.model.getBoard().getPossibleCells()) {
	    			i.setDisable(false);

		    		if (currentSelect != null) {

		    			for (Cell ii: view.model.getBoard().getPossibleDestinations(currentSelect)) {
		    				ii.setDisable(false);
		    				this.currentSelect.setDisable(true);
		    			
		    				
		    			}
		    		}
	    		}
    	}
    	if (view.model.getBoard().isGameOver()) {
    		
    		//view.setMessageLabel((view.model.getBoard().getWinner()).toString() + "WON");
    		this.gameOver();
    
    	}

    	
    }
    


    
    public void disableAllCells() {
    	for (Cell i: view.model.getBoard().getAllCells()) {
    		i.setDisable(true);
    		}
    	
    	}
    
    /**
     * Handles Cell clicks and updates the board accordingly
     * When a Cell gets clicked the following must be handled:
     *  - If it's a valid piece that the player can move, select the piece and update the board
     *  - If it's a destination for a selected piece to move, perform the move and update the board
     * @param actionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent) { // TODO
    	Cell hi =(Cell) actionEvent.getSource();
    	
    	
    	if (this.currentSelect == null) {
	    	if (view.model.getBoard().getPossibleCells().contains(hi)) {
	    		this.currentSelect = hi;
	    		this.currentSelect.setDisable(false);
	    		hi.setDisable(false);
	    		updateCells();
	    		view.runMove();
	    	}
	    	
	    	
	    	
    	}
    	else {
    		if (this.currentSelect.getPiece().getType() != view.model.getBoard().getTurn()) {
    			currentSelect = null;
    		}
    		
    		if (view.model.getBoard().getPossibleDestinations(this.currentSelect).contains(hi)) {
    			Move move = new Move( this.currentSelect, hi);
    			view.model.move(move);
    			currentSelect = null;
    			updateCells();
    			view.runMove();
    			
    			
    		}

    	}

    }
    public void gameOver() {
    	this.disableAllCells();
    }
}
