import { useState } from "react";
import { useNavigate } from 'react-router-dom';
import hangmanImage from '../../images/hangman.png';
import "./Login.css";


const LoginGiver = () => {

    const [username, setUsername] = useState('');
    const [isPending, setIsPending] = useState(false);
    const navigate = useNavigate();

    const navigateToGuesserUsername = () => {
        navigate('/multi-player/guesser', { state: { giver: username } });
    }


    const handleSubmit = (e) => {
        setIsPending(true);
        e.preventDefault();
        setTimeout(() => {
            fetch('http://localhost:8080/api/v1/players/' + username, {
                method: 'POST'
            }).then(() => {
                setIsPending(false);
                navigateToGuesserUsername();
            })
        }, 500)
    }

    return (
        <div className="login">
            <div className="login-form">
                <h2>Multi-Player Game</h2>
                <p>Welcome, Word Giver!</p>
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

export default LoginGiver;