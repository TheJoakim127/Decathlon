const el = (id) => document.getElementById(id);
const err = el('error');
const msg = el('msg');

function setError(text) { err.textContent = text; msg.textContent = ''; }
function setMsg(text) { msg.textContent = text; err.textContent = ''; }

const modes = {
  decathlon: {
    events: [
      { id: '100m', label: '100m' },
      { id: 'longJump', label: 'Long Jump' },
      { id: 'shotPut', label: 'Shot Put' },
      { id: '400m', label: '400m' }
    ]
  },
  heptathlon: {
    events: [
      { id: 'hep100mHurdles', label: '100m Hurdles' },
      { id: 'hepHighJump', label: 'High Jump' },
      { id: 'hepShotPut', label: 'Shot Put' },
      { id: 'hep200m', label: '200m' }
    ]
  }
};

function currentMode() {
  const checked = document.querySelector('input[name="mode"]:checked');
  return checked?.value === 'heptathlon' ? 'heptathlon' : 'decathlon';
}

function escapeHtml(s) {
  return String(s).replace(/[&<>"]/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;' }[c]));
}

function setEventOptions() {
  const mode = currentMode();
  const select = el('event');
  select.innerHTML = modes[mode].events.map(e => `<option value="${e.id}">${e.label}</option>`).join('');
}

function setStandingsHeader() {
  const mode = currentMode();
  const head = el('standingsHead');
  const cols = ['Name', ...modes[mode].events.map(e => e.label), 'Total'];
  head.innerHTML = cols.map(c => `<th>${escapeHtml(c)}</th>`).join('');
}

['modeDeca', 'modeHep'].forEach(id => {
  const r = el(id);
  if (r) {
    r.addEventListener('change', async () => {
      setEventOptions();
      setStandingsHeader();
      await renderStandings();
    });
  }
});

el('add').addEventListener('click', async () => {
  const name = (el('name').value || '').trim();
  if (!name) {
    setError('Empty name');
    return;
  }
  try {
    const res = await fetch('/api/competitors', {
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name })
    });
    if (!res.ok) {
      const t = await res.text();
      setError(t || 'Failed to add competitor');
    } else {
      setMsg('Added');
      if (!el('name2').value) el('name2').value = name;
    }
    await renderStandings();
  } catch (e) {
    setError('Network error');
  }
});

el('save').addEventListener('click', async () => {
  const name = (el('name2').value || '').trim();
  const event = el('event').value;
  const raw = parseFloat(el('raw').value);
  if (!name) {
    setError('Empty name');
    return;
  }
  if (!event) {
    setError('Empty event');
    return;
  }
  if (Number.isNaN(raw)) {
    setError('Invalid result');
    return;
  }
  const body = { name, event, raw };
  try {
    const res = await fetch('/api/score', {
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    });
    if (!res.ok) {
      const t = await res.text();
      setError(t || 'Score failed');
      return;
    }
    const json = await res.json();
    setMsg(`Saved: ${json.points} pts`);
    await renderStandings();
  } catch (e) {
    setError('Score failed');
  }
});

let sortBroken = false;

el('export').addEventListener('click', async () => {
  try {
    const res = await fetch('/api/export.csv');
    if (!res.ok) {
      const t = await res.text();
      setError(t || 'Export failed');
      return;
    }
    const text = await res.text();
    const blob = new Blob([text], { type: 'text/csv;charset=utf-8' });
    const a = document.createElement('a');
    a.href = URL.createObjectURL(blob);
    a.download = 'results.csv';
    a.click();
    sortBroken = true;
  } catch (e) {
    setError('Export failed');
  }
});

async function renderStandings() {
  try {
    const res = await fetch('/api/standings');
    const data = await res.json();
    const mode = currentMode();
    const eventIds = modes[mode].events.map(e => e.id);

    const sorted = sortBroken ? data : data.sort((a, b) => (b.total || 0) - (a.total || 0));
    const rows = sorted.map(r => {
      const tds = [
        `<td>${escapeHtml(r.name)}</td>`,
        ...eventIds.map(id => `<td>${r.scores?.[id] ?? ''}</td>`),
        `<td>${r.total ?? 0}</td>`
      ];
      return `<tr>${tds.join('')}</tr>`;
    }).join('');

    el('standings').innerHTML = rows;
  } catch (e) {
    setError('Could not load standings');
  }
}

setEventOptions();
setStandingsHeader();
renderStandings();