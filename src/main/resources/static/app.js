const el = (id) => document.getElementById(id);
const err = el('error');
const msg = el('msg');

function setError(text) { err.textContent = text; msg.textContent = ''; }
function setMsg(text) { msg.textContent = text; err.textContent = ''; }

const modes = {
  decathlon: {
    events: [
      { id: '100m', label: '100m (s)' },
      { id: 'longJump', label: 'Long Jump (cm)' },
      { id: 'shotPut', label: 'Shot Put (m)' },
      { id: 'highJump', label: 'High Jump (cm)' },
      { id: '400m', label: '400m (s)' },
      { id: '110mHurdles', label: '110m Hurdles (s)' },
      { id: 'discusThrow', label: 'Discus Throw (m)' },
      { id: 'poleVault', label: 'Pole Vault (cm)' },
      { id: 'javelinThrow', label: 'Javelin Throw (m)' },
      { id: '1500m', label: '1500m (s)' }
    ]
  },
  heptathlon: {
    events: [
      { id: 'hep100mHurdles', label: '100m Hurdles (s)' },
      { id: 'hepHighJump', label: 'High Jump (cm)' },
      { id: 'hepShotPut', label: 'Shot Put (m)' },
      { id: 'hep200m', label: '200m (s)' },
      { id: 'hepLongJump', label: 'Long Jump (cm)' },
      { id: 'hepJavelinThrow', label: 'Javelin Throw (m)' },
      { id: 'hep800m', label: '800m (s)' }
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
  const cols = ['Place', 'Name', ...modes[mode].events.map(e => e.label), 'Total'];
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
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name })
    });

    if (!res.ok) {
      const t = await res.text();
      setError(t || 'Failed to add competitor');
    } else {
      const json = await res.json();
      setMsg(`Added: ${json.name}`);
      el('name').value = json.name;
      if (!el('name2').value) el('name2').value = json.name;
    }

    await renderStandings();
  } catch (e) {
    setError('Network error');
  }
});

el('save').addEventListener('click', async () => {
  const name = (el('name2').value || '').trim();
  const event = el('event').value;
  const result = parseFloat(el('raw').value);

  if (!name) {
    setError('Empty name');
    return;
  }

  if (!event) {
    setError('Empty event');
    return;
  }

  if (Number.isNaN(result)) {
    setError('Invalid result');
    return;
  }

  const body = { name, event, result };

  try {
    const res = await fetch('/api/score', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    });

    if (!res.ok) {
      const t = await res.text();
      setError(t || 'Score failed');
      return;
    }

    const json = await res.json();
    setMsg(`Saved: ${json.points} pts`);
    el('name2').value = json.name;
    await renderStandings();
  } catch (e) {
    setError('Score failed');
  }
});

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
  } catch (e) {
    setError('Export failed');
  }
});

el('importBtn').addEventListener('click', async () => {
  const file = el('importFile').files?.[0];
  if (!file) {
    setError('Choose a CSV file');
    return;
  }

  try {
    const text = await file.text();
    const res = await fetch('/api/import.csv', {
      method: 'POST',
      headers: { 'Content-Type': 'text/plain;charset=utf-8' },
      body: text
    });

    if (!res.ok) {
      const t = await res.text();
      setError(t || 'Import failed');
      return;
    }

    setMsg('Import completed');
    await renderStandings();
  } catch (e) {
    setError('Import failed');
  }
});

async function renderStandings() {
  try {
    const res = await fetch('/api/standings');
    if (!res.ok) {
      setError('Could not load standings');
      return;
    }

    const data = await res.json();
    const mode = currentMode();
    const eventIds = modes[mode].events.map(e => e.id);

    const rows = data.map(r => {
      const tds = [
        `<td>${r.rank ?? ''}</td>`,
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