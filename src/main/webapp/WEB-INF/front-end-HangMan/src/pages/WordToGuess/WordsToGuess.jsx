import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "./WordToGuess.css";
import Loader from "../../Components/Loader/Loader";
import { wordValidation } from '../../utils/WordValidation';
import Error from './../Errors/Error';

const WordToGuess = () => {

    const [word, setWord] = useState('');
    const [category, setCategory] = useState('');
    const [categories, setCategories] = useState([]);
    const location = useLocation();
    const { giver, guesser } = location.state || {};
    const [game, setGame] = useState(null);
    const navigate = useNavigate();
    const [error, setError] = useState(null);
    const [isPending, setIsPending] = useState(false);
    const [validationError, setValidationError] = useState('');

    const dto = {
        multiPlayerGameInputDTO: {
            giverUsername: giver,
            guesserUsername: guesser,
            wordToGuess: word,
            category: category
        }
    }

    useEffect(() => {
        setIsPending(true);
        fetch('http://localhost:8080/api/v1/categories')
            .then(res => {
                return res.json();
            }).then((data) => {
                setIsPending(false);
                setCategories(data);
            })
    }, []);


    const handleSubmit = (e) => {
        e.preventDefault();
        const validationMessage = wordValidation(word);
        if (validationMessage) {
            setValidationError(validationMessage);
            setIsPending(false);
            return;
        }

        fetch('http://localhost:8080/api/v1/games', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(dto)
        })
            .then(res => {
                if (!res.ok) {
                    return res.json().then((errorData) => {
                        throw { message: errorData.message, ...errorData };
                    });
                }
                return res.json();
            }).then(data => {
                navigate('/multi-player/games', {
                    state: {
                        gameDTO: data
                    }
                });
            }).catch(err => setError(err));
    }

    if (error) {
        return <Error error={error} />;
    }

    return (

        <div className="word-giving">
            {isPending ? (
                <div className="loader-container">
                    <Loader />
                </div>
            ) : (<form onSubmit={handleSubmit}>
                <label>Word To Guess:</label>
                <input type="text"
                    required
                    value={word}
                    onChange={(e) => setWord(e.target.value)}>
                </input>
                <select
                    required
                    value={category}
                    onChange={(e) => setCategory(e.target.value)}>
                    <option value="" disabled>Select Category</option>
                    {categories.map((c) => (
                        <option key={c.id} value={c.name}>{c.name}</option>
                    ))}
                </select>
                {validationError && <p className="validation-error">{validationError}</p>}
                {!validationError && <p className="validation-error"></p>}
                <button>Submit</button>
            </form>)}
        </div>
    );
}

export default WordToGuess;