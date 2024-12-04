import { useState } from "react";
import { useNavigate, useLocation } from 'react-router-dom';
import hangmanImage from '../../images/hangman.png';
import "./Login.css";
import { validateUsername } from "../../utils/ValidationUtils";
import Error from './../Errors/Error'


const LoginGuesser = () => {

    const [username, setUsername] = useState('');
    const [isPending, setIsPending] = useState(false);
    const navigate = useNavigate();
    const location = useLocation();
    const { giver } = location.state || {};
    const [validationErr, setValidationErr] = useState('');
    const [error, setError] = useState(null);

    const navigateToEnterWord = () => {
        navigate('/multi-player/words', { state: { guesser: username, giver } });
    }

    const handleSubmit = (e) => {
        setIsPending(true);
        e.preventDefault();
        const validationMessage = validateUsername(username);
        if (validationMessage) {
            setValidationErr(validationMessage);
            setIsPending(false);
            return;
        }
        setTimeout(() => {
            fetch('http://localhost:8080/api/v1/players/' + username, {
                method: 'POST'
            }).then((res) => {
                if (!res.ok) {
                    return res.json().then((errorData) => {
                        throw { message: errorData.message, ...errorData };
                    });
                }
                setIsPending(false);
                navigateToEnterWord();
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
                    {validationErr && <p className="validation-error">{validationErr}</p>}
                    {!validationErr && <p className="validation-error"></p>}
                    {!isPending && <button>Submit</button>}
                    {isPending && <button disabled>Submitting...</button>}
                </form>
            </div>
        </div>
    );
}

export default LoginGuesser;