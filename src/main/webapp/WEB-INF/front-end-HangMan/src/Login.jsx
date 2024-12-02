import { useState } from "react";
import { useNavigate } from 'react-router-dom';
import hangmanImage from './images/hangman.png';

const Login = () => {

    const [username, setUsername] = useState('');
    const [isPending, setIsPending] = useState(false);
    const navigate = useNavigate();
    const [game, setGame] = useState(null);

    const navigateToSinglePlayerGame = () => {
        fetch('http://localhost:8080/api/v1/games', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                playerDTO: {
                    username: username
                }
            })
        })
            .then(res => {
                return res.json();
            }).then(data => {
                setGame(data);
                navigate('/single-player/games', { state: { gameId: data.gameId } });
            })
    }

    const handleSubmit = (e) => {
        setIsPending(true);
        e.preventDefault();
        setTimeout(() => {
            fetch('http://localhost:8080/api/v1/players/' + username, {
                method: 'POST'
            }).then(() => {
                setIsPending(false);
                navigateToSinglePlayerGame();
            })
        }, 500)
    }

    return (
        <div className="login">
            <div className="login-form">
                <h2>Single-Player Game</h2>
                <img src={hangmanImage} alt="" />
                <form onSubmit={handleSubmit}>
                    <label>Enter username:</label>
                    <input
                        type="text"
                        required
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    ></input>
                    {!isPending && <button>Submit</button>}
                    {isPending && <button disabled>Submitting...</button>}
                </form>
            </div>
        </div>
    );
}

export default Login;