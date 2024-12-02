import { useState } from "react";
import { useNavigate, useLocation } from 'react-router-dom';
import hangmanImage from '../../images/hangman.png';
import "./Login.css";


const LoginGuesser = () => {

    const [username, setUsername] = useState('');
    const [isPending, setIsPending] = useState(false);
    const navigate = useNavigate();
    const location = useLocation();
    const { giver } = location.state || {};

    const navigateToEnterWord = () => {
        navigate('/multi-player/words', { state: { guesser: username, giver } });
    }

    const handleSubmit = (e) => {
        setIsPending(true);
        e.preventDefault();
        setTimeout(() => {
            fetch('http://localhost:8080/api/v1/players/' + username, {
                method: 'POST'
            }).then(() => {
                setIsPending(false);
                navigateToEnterWord();
            })
        }, 500)
    }

    return (
        <div className="login">
            <div className="login-form">
                <h2>Multi-Player Game</h2>
                <p>Welcome, Word Guesser!</p>
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

export default LoginGuesser;