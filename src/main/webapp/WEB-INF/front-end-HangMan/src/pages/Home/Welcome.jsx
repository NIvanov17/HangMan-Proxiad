import hangmanImage from '../../images/hangman.png';
import { useNavigate } from 'react-router-dom';
import { useState, useEffect } from "react";
import "./Home.css"

const Welcome = () => {
    const navigate = useNavigate();
    const [username, setUsername] = useState("");

    const startSinglePlayerGame = () => {
        navigate('/single-player/username');
    }

    const startMultiPlayerGame = () => {
        navigate('/multi-player/giver')
    }

    useEffect(() => {
        const storedUsername = localStorage.getItem("username");

        if (storedUsername) {
            setUsername(storedUsername);
        } else {
            navigate("/login");
        }
    }, []);

    return (
        <div className="home">
            <h1>Hang Man Game</h1>
            <h2>Welcome, {username}!</h2>
            <img src={hangmanImage} alt="Hangman Icon"></img>
            <div className="start-gameButtons">
                <button onClick={startSinglePlayerGame}>Single Player Game</button>
                <button onClick={startMultiPlayerGame}>MultiPlayer Game</button>
            </div>
        </div>
    );
}

export default Welcome;