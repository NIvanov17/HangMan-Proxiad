import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import img0 from './images/0.png';
import img1 from './images/1.png';
import img2 from './images/2.png';
import img3 from './images/3.png';
import img4 from './images/4.png';
import img5 from './images/5.png';
import img6 from './images/6.png';
import Loader from "./Loader";

const MultiPlayerGame = () => {
    const letters = "abcdefghijklmnopqrstuvwxyz".split("");
    const images = [img0, img1, img2, img3, img4, img5, img6];
    const location = useLocation();
    const { gameDTO, gameId } = location.state || {};
    const [game, setGame] = useState(null);
    const [isPending, setIsPending] = useState(false);
    const navigate = useNavigate();
    const restartSinglePlayerGame = () => navigate('/single-player/username');
    const restartMultiPlayerGame = () => navigate('/multi-player/giver');

    useEffect(() => {
        const id = gameDTO ? gameDTO.gameId : gameId;
        fetch(`http://localhost:8080/api/v1/games?id=${id}`)
            .then(res => {
                return res.json();
            })
            .then(data => {
                setGame(data);
                console.log(data);
            })
    }, [])

    const handleGuess = (e, letter) => {
        e.preventDefault();
        setIsPending(true);
        console.log(gameDTO);

        const handleGuessDTO = {
            playerDTO: {
                username: game.guesser.username
            },
            guessDTO: {
                guess: letter
            }
        }
        fetch(`http://localhost:8080/api/v1/games/${game.gameId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(handleGuessDTO)
        }).then(res => res.json())
            .then((updatedGame) => {
                setGame(updatedGame);
                setIsPending(false);
            });
    }

    if (!game) {
        return <Loader />;
    }

    return (
        <div className="game">
            <h2>Word to Guess: {game.wordProgress}</h2>
            <p>Tries left: {game.triesLeft}</p>
            <p>Mode: {game.gameMode}</p>
            <h2>{game.gameStatus}</h2>
            <div className="letters">
                {
                    letters.map(letter => (
                        <form
                            key={letter}
                            onSubmit={(e) => handleGuess(e, letter)}>
                            <button
                                type="submit"
                                value={letter}
                                disabled={game.usedChars.includes(letter) || game.finished}
                            >{letter.toUpperCase()}</button>
                        </form>
                    ))
                }
            </div>
            <img src={images[game.triesLeft]} alt="triesLeftImg"></img>
            {game.finished &&
                ("Single Player" === game.gameMode ?
                    <div className="restart-btn">
                        <button onClick={restartSinglePlayerGame}>Restart Game</button>
                    </div> :
                    <div className="restart-btn">
                        <button onClick={restartMultiPlayerGame}>Restart Game</button>
                    </div>)
            }
        </div >
    );
}

export default MultiPlayerGame;