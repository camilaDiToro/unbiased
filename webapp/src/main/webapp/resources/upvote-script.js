async function postData(url = '', data = {}) {
    console.log(data)
    // Default options are marked with *
    const response = await fetch(url, {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        mode: 'cors', // no-cors, *cors, same-origin
        cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
        credentials: 'same-origin', // include, *same-origin, omit
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data) // body data type must match "Content-Type" header
    });
    let text = await response.text()
    console.log(text)
    let json = JSON.parse(text);
    json.ok = response.ok;
    return json; // parses JSON response into native JavaScript objects
}



async function handleClick(img) {
    const src = img.src
    const URL = img.getAttribute('url')
    const newsId = img.parentElement.getAttribute('news-id')

    var number
    img.parentElement.childNodes.forEach(e => {
        if (e.id === "rating")
            number = e
    })
    var newSrc

    const removeActionAndUpdate = r => {
        if (!r.active) {
            newSrc = src.replace(/-clicked/g, "")
            img.src = newSrc
            number.className = ''
            number.textContent = r.upvotes
        }
    }

    const addActionAndRemoveOpposite = (r, oppositeId, numberClassname) => {
        if (r.active) {
            const aux = src.split('.')
            newSrc = aux[0] + '-clicked' + '.' + aux[1]
            img.src = newSrc
            var arrow;
            img.parentElement.childNodes.forEach(e => {
                if (e.id === oppositeId)
                    arrow = e
            })
            const otherSrc = arrow.src;
            arrow.setAttribute('src', otherSrc.replace(/-clicked/g, ""));
            number.className = numberClassname
            number.textContent = r.upvotes
        }
    }

    if (src.includes('clicked')) {
        // currently upvoted -> remove upvote
        postData(URL, {active: false, newsId: newsId}).then(removeActionAndUpdate)
    }

    else {
        if (src.includes('upvote')) {
            postData(URL, {active: true, newsId: newsId}).then(r => addActionAndRemoveOpposite(r, 'downvote', 'upvoted'))
        }
        else {
            postData(URL, {active: true, newsId: newsId}).then(r => addActionAndRemoveOpposite(r, 'upvote', 'downvoted'))
        }
    }

}

async function handleBookmarkClick(bImg) {
    const src = bImg.src
    const URL = bImg.getAttribute('url')
    // const newsId = bImg.parentElement.getAttribute('url')

    console.log(URL)

    const setBookmarkColor = (response) => {
        if (response.ok) {
            if (response.active) {
                if (!src.includes('-clicked')) {
                    const aux = src.split('.')
                    bImg.src = aux[0] + '-clicked' + '.' + aux[1]
                }
            }

            else {
                if (src.includes('-clicked')) {
                    bImg.setAttribute('src', src.replace(/-clicked/g, ""));
                }
            }
        }
    }

    await postData(URL, {}).then(r => setBookmarkColor(r))

}
