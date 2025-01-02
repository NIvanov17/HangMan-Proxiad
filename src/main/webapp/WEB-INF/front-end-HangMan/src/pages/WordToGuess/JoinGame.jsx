import { useState } from "react";
import { useNavigate } from 'react-router-dom';
import "./JoinGame.css";

const JoinGame = () => {

    const [token, setToken] = useState('');
    const [validationError, setValidationError] = useState('');
    const [isPending, setIsPending] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        setIsPending(true);
        e.preventDefault();
        const gameTokenDTO = {
            token: token
        };

        fetch(`http://localhost:8080/api/v2/games/game`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${sessionStorage.getItem("token")}`
            },
            body: JSON.stringify(gameTokenDTO)
        }).then(res => res.json())
            .then(data => {
                console.log(data.gameId);
                navigate('/multi-player/games', { state: { gameId: data.gameId } });
            }
            )
    }

    return (
        <div className="join-game">
            <h2>Join game with code</h2>
            <form onSubmit={handleSubmit}>
                <label>Enter code:</label>
                <input
                    type="text"
                    required
                    value={token}
                    onChange={(e) => setToken(e.target.value)}
                />
                {validationError && <p id="validation-error">{validationError}</p>}
                {!isPending && <button>Submit</button>}
            </form>
        </div>
    );
}

export default JoinGame;