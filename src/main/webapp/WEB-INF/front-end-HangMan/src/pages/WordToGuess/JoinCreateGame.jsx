import { useNavigate } from 'react-router-dom';
import "./JoinCreateGame.css";

const JoinCreateGame = () => {

    const navigate = useNavigate();

    const createGame = () => {
        navigate('/multi-player/words');
    }

    const joinGame = () => {
        navigate('/multi-player/join');
    }

    return (
        <div className="joinCreateGame">
            <button onClick={createGame}>Create new game</button>
            <button onClick={joinGame}>Join with code</button>
        </div>
    );
}

export default JoinCreateGame;