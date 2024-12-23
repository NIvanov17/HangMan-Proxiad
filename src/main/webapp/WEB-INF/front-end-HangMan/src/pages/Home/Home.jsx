import hangmanImage from '../../images/hangman.png';
import { useNavigate } from 'react-router-dom';
import "./Home.css"
import { useEffect, useState } from 'react';

const Home = () => {
    const navigate = useNavigate();
    const [token, setToken] = useState(null);

    const startSinglePlayerGame = () => {
        if (token) {
            navigate('/single-player/games');
        } else {
            navigate('/login');
        }
    }

    const startMultiPlayerGame = () => {
        if (token) {
            navigate('/multi-player/words');
        } else {
            navigate('/login');
        }
    }

    useEffect(() => {
        const storedToken = sessionStorage.getItem("token");
        if (storedToken) {
            setToken(storedToken);
        } else {
            setToken(null);
        }
    }, [])

    return (
        <div className="home">
            <h1>Hang Man Game</h1>
            <img src={hangmanImage} alt="Hangman Icon"></img>
            <div className="start-gameButtons">
                <button onClick={startSinglePlayerGame}>Single Player Game</button>
                <button onClick={startMultiPlayerGame}>MultiPlayer Game</button>
            </div>
        </div>
    );
}

export default Home;