package passoff.chess.game;

import chess.ChessGame;
import chess.ChessPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static passoff.chess.TestUtilities.*;

public class ValidMovesTests {
    @Test
    @DisplayName("Check Forces Movement")
    public void forcedMove() {

        var game = new ChessGame();
        game.setBoard(loadBoard("""
                    | | | | | | | | | | | |
                    | | | | | | | | | | | |
                    | | | | | | | | | | | |
                    | | | | | | | | | | | |
                    | |P| | | | | | | | | |
                    | | | | | |K| | | | | |
                    | | |l| | | | | | | | |
                    | | | | | | | | | | | |
                    | | | | | |k| | | | | |
                    | | |p|r| | | | | | | |
                    """));

        // Knight moves
        ChessPosition knightPosition = new ChessPosition(4, 3);
        var validMoves = loadMoves(knightPosition, new int[][]{{3, 5}, {6, 2}});
        assertMoves(game, validMoves, knightPosition);

        // Picket Moves
        ChessPosition picketPosition = new ChessPosition(1, 3);
        validMoves = loadMoves(picketPosition, new int[][]{{3, 5}});
        assertMoves(game, validMoves, picketPosition);

        // Rook Moves
        ChessPosition rookPosition = new ChessPosition(1, 4);
        validMoves = loadMoves(rookPosition, new int[][]{{4, 4}});
        assertMoves(game, validMoves, rookPosition);
    }


    @Test
    @DisplayName("Piece Partially Trapped")
    public void moveIntoCheck() {

        var game = new ChessGame();
        game.setBoard(loadBoard("""
                    | | | | | | | | | | | |
                    | | | | | | | | | | | |
                    | | | | | | | | | | | |
                    | | | | | | | | | | | |
                    | | | | | | | | | | | |
                    | |r| | | |R| |K| | | |
                    | | | | | | | | | | | |
                    | | | | | | | | | | | |
                    | | | | | | | | | | | |
                    | | | | | | | | | | | |
                    """));

        ChessPosition rookPosition = new ChessPosition(5, 6);
        var validMoves = loadMoves(rookPosition, new int[][]{
                {5, 7}, {5, 5}, {5, 4}, {5, 3}, {5, 2}
        });

        assertMoves(game, validMoves, rookPosition);
    }

    @Test
    @DisplayName("Piece Completely Pinned")
    public void rookPinnedToKing() {

        var game = new ChessGame();
        game.setBoard(loadBoard("""
                    | | | | | | | | | | | |
                    | | | | | | | | | | | |
                    | | | | | | | |P| | | |
                    | | | | | | | | | | | |
                    | | | | | | | | | | | |
                    | | | | | | | | | | | |
                    | | | |r| | | | | | | |
                    | | | | | | | | | | | |
                    | |k| | | | | | | | | |
                    | | | | | | | | | | | |
                    """));

        ChessPosition position = new ChessPosition(4, 4);
        Assertions.assertTrue(game.validMoves(position).isEmpty(),
                "ChessGame validMoves returned valid moves for a pinnned piece");
    }


    @Test
    @DisplayName("Pieces Cannot Eliminate Check")
    public void kingInDanger() {

        var game = new ChessGame();
        game.setBoard(loadBoard("""
                    | | | | | | | |P| | | |
                    | | | | | | | | | | | |
                    |R| | | | | | | | | | |
                    | | | |k| | | |p| | | |
                    | | | | |O| | | | | | |
                    | | |R|l| | | | | | | |
                    | | | | | | | | | | | |
                    | | | | | | | |r| | | |
                    | | | | | |o| | | | | |
                    | |c| | | | | | | | | |
                    """));

        //get positions
        ChessPosition kingPosition = new ChessPosition(7, 4);
        ChessPosition pawnPosition = new ChessPosition(2, 6);
        ChessPosition picketPosition = new ChessPosition(7, 8);
        ChessPosition camelPosition = new ChessPosition(1, 2);
        ChessPosition knightPosition = new ChessPosition(5, 4);
        ChessPosition rookPosition = new ChessPosition(3, 8);


        var validMoves = loadMoves(kingPosition, new int[][]{{6, 5}});

        assertMoves(game, validMoves, kingPosition);

        //make sure teams other pieces are not allowed to move
        Assertions.assertTrue(game.validMoves(pawnPosition).isEmpty(),
                "ChessGame validMoves returned valid moves for a trapped piece");
        Assertions.assertTrue(game.validMoves(picketPosition).isEmpty(),
                "ChessGame validMoves returned valid moves for a trapped piece");
        Assertions.assertTrue(game.validMoves(camelPosition).isEmpty(),
                "ChessGame validMoves returned valid moves for a trapped piece");
        Assertions.assertTrue(game.validMoves(knightPosition).isEmpty(),
                "ChessGame validMoves returned valid moves for a trapped piece");
        Assertions.assertTrue(game.validMoves(rookPosition).isEmpty(),
                "ChessGame validMoves returned valid moves for a trapped piece");
    }


    @Test
    @DisplayName("King Cannot Move Into Check")
    public void noPutSelfInDanger() {

        var game = new ChessGame();
        game.setBoard(loadBoard("""
                    | | | | | | | | | | | |
                    | | | | | | | | | | | |
                    | | | | | | | | | | | |
                    | | | | | | | | | | | |
                    | | | | | | | | | | | |
                    | | | | | | | | | | | |
                    | | | | | |k| | | | | |
                    | | | | | | | | | | | |
                    | | | | | |K| | | | | |
                    | | | | | | | | | | | |
                    """));

        ChessPosition position = new ChessPosition(2, 6);
        var validMoves = loadMoves(position, new int[][]{
                {1, 5}, {1, 6}, {1, 7}, {2, 5}, {2, 7},
        });
        assertMoves(game, validMoves, position);
    }
}
