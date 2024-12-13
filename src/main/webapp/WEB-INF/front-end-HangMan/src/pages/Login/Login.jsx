import { useState } from "react";
import { useNavigate } from 'react-router-dom';
import hangmanImage from '../../images/hangman.png';
import Error from './../Errors/Error';
import "./Login.css";
import { validateUsername } from "../../utils/ValidationUtils";

const Login = () => {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [isPending, setIsPending] = useState(false);
    const navigate = useNavigate();
    const [game, setGame] = useState(null);
    const [error, setError] = useState(null);
    const [validationError, setValidationError] = useState('');


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
                if (!res.ok) {
                    return res.json().then((errorData) => {
                        throw new Error(errorData.message);
                    });
                }
                return res.json();
            }).then(data => {
                setGame(data);
                navigate('/single-player/games', { state: { gameId: data.gameId } });
            }).catch(err => setError(err))
    }

    const handleSubmit = (e) => {
        setIsPending(true);
        e.preventDefault();
        const validationMessage = validateUsername(username);
        if (validationMessage) {
            setValidationError(validationMessage);
            setIsPending(false);
            return;
        }

        setTimeout(() => {
            fetch('http://localhost:8080/api/v1/players/' + username, {
                method: 'POST'
            }).then((res) => {
                setIsPending(false);
                if (!res.ok) {
                    return res.json().then((errorData) => {
                        throw { message: errorData.message, ...errorData };
                    });
                }
                navigateToSinglePlayerGame();
            }).catch(err => {
                setError(err)
            })
        }, 500)
    }

    if (error) {
        return <Error error={error} />;
    }

    return (
        <div className="login">
            <div className="login-form">
                <h2>User Log In</h2>
                <img src={hangmanImage} alt="" />
                <form onSubmit={handleSubmit}>
                    <label>Enter username:</label>
                    <input
                        type="text"
                        required
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    ></input>
                    <label>Password:</label>
                    <input
                        type="password"
                        required
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    ></input>
                    {validationError && <p className="validation-error">{validationError}</p>}
                    {!isPending && <button>Submit</button>}
                    {isPending && <button disabled>Submitting...</button>}
                </form>
            </div>
        </div>
    );
}

export default Login;