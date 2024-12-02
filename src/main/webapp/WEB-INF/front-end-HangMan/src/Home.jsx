import hangmanImage from './images/hangman.png';
import { useNavigate } from 'react-router-dom';

const Home = () => {
    const navigate = useNavigate();

    const startSinglePlayerGame = () => {
        navigate('/single-player/username');
    }

    const startMultiPlayerGame = () => {
        navigate('/multi-player/giver')
    }

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